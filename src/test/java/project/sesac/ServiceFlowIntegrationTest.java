package project.sesac;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import project.sesac.domain.Board;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.service.BoardService;
import project.sesac.service.InformationSyncService;
import project.sesac.service.LastService;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "sesac.crawler.enabled=false"
})
class ServiceFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private BoardService boardService;

    @MockBean
    private LastService lastService;

    @MockBean
    private InformationSyncService informationSyncService;

    @BeforeEach
    void setUp() {
        doNothing().when(lastService).lastLogic(org.mockito.ArgumentMatchers.anyLong());
        when(informationSyncService.refreshInformation()).thenReturn(3);
    }

    @Test
    void loginPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void signinAllowsAccessToMainAndMypage() throws Exception {
        MockHttpSession session = login("chung", "1234");

        mockMvc.perform(get("/main").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("name", "level", "mission1", "mission2", "list1", "list2"));

        MvcResult mypageResult = mockMvc.perform(get("/mypage").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage"))
                .andExpect(model().attributeExists("loginId", "name", "level", "chooseRole"))
                .andReturn();

        String responseHtml = mypageResult.getResponse().getContentAsString();
        assertThat(responseHtml).doesNotContain("value=\"1234\"");
    }

    @Test
    void mypageUpdateChangesPreferenceWithoutOverwritingPasswordWhenBlank() throws Exception {
        MockHttpSession session = login("ddding", "1234");
        Member member = memberService.findByLoginId("ddding").get(0);

        mockMvc.perform(post("/mypage/update")
                        .session(session)
                        .param("loginPassword", "")
                        .param("chooseRole", "job"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main"));

        Member updatedMember = memberService.findById(member.getId());
        MemberInfo updatedInfo = memberInfoService.findById(member.getId()).orElseThrow();

        assertThat(updatedMember.getLoginPassword()).isEqualTo("1234");
        assertThat(updatedInfo.getChooseRole()).isEqualTo(1);
    }

    @Test
    void boardJoinAndDisconnectFlowWorks() throws Exception {
        MockHttpSession session = login("ddding", "1234");

        mockMvc.perform(get("/board/join/100").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/content/100"));

        Board joinedBoard = boardService.findById(100L);
        assertThat(joinedBoard.getAgent()).isEqualTo(2);

        mockMvc.perform(get("/board/disconnect/100").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/content/100"));

        Board disconnectedBoard = boardService.findById(100L);
        assertThat(disconnectedBoard.getAgent()).isEqualTo(1);
    }

    @Test
    void boardAuthorCanScheduleDeadline() throws Exception {
        MockHttpSession session = login("chung", "1234");

        mockMvc.perform(get("/board/deadline/100").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/content/100"))
                .andExpect(flash().attributeExists("errorMessage"));

        Board board = boardService.findById(100L);
        assertThat(board.isDeadlineScheduled()).isTrue();
        assertThat(board.isAgentFull()).isTrue();
    }

    @Test
    void informationPagesAndRefreshEndpointWorkForAuthenticatedUser() throws Exception {
        MockHttpSession session = login("chung", "1234");

        mockMvc.perform(get("/information/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("information"))
                .andExpect(model().attributeExists("posts", "count"));

        mockMvc.perform(get("/information/getCareAndJobInformation").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/information/1"))
                .andExpect(flash().attribute("errorMessage", "3개의 정보를 새로고침했습니다."));
    }

    @Test
    void maintenanceEndpointsRequireValidToken() throws Exception {
        mockMvc.perform(post("/main/updateMission").param("token", "wrong-token"))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/main/memberInfoChange").param("token", "wrong-token"))
                .andExpect(status().isNotFound());
    }

    private MockHttpSession login(String loginId, String password) throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login/signin")
                        .param("loginId", loginId)
                        .param("loginPassword", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main"))
                .andReturn();

        HttpSession session = loginResult.getRequest().getSession(false);
        assertThat(session).isInstanceOf(MockHttpSession.class);
        return (MockHttpSession) session;
    }
}
