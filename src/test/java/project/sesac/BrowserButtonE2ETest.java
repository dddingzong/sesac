package project.sesac;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import project.sesac.domain.Board;
import project.sesac.service.BoardService;
import project.sesac.service.InformationSyncService;
import project.sesac.service.LastService;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "sesac.crawler.enabled=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BrowserButtonE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private BoardService boardService;

    @MockBean
    private LastService lastService;

    @MockBean
    private InformationSyncService informationSyncService;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        doNothing().when(lastService).lastLogic(anyLong());
        when(informationSyncService.refreshInformation()).thenReturn(3);

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1440,1600");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void buttonsWorkInActualBrowserFlow() {
        String baseUrl = "http://localhost:" + port;
        String loginId = "e2e_" + System.currentTimeMillis();
        String boardTitle = "e2e-board-" + System.currentTimeMillis();

        driver.get(baseUrl + "/login");

        click(By.cssSelector(".show-signup"));
        type(By.id("name"), "브라우저테스트");
        type(By.id("loginId"), loginId);
        type(By.id("loginPassword"), "1234");
        type(By.id("loginPassword_confirm"), "1234");
        select(By.id("chooseRole"), "all");
        click(By.id("submit-btn"));
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));

        type(By.id("loginId"), loginId);
        type(By.id("loginPassword"), "1234");
        click(By.id("submit-btn"));
        wait.until(ExpectedConditions.urlContains("/main"));

        click(By.cssSelector("form[action='/mission/complete'] button"));
        wait.until(driver -> driver.getPageSource().contains("성공"));
        assertThat(driver.getPageSource()).contains("성공");

        driver.get(baseUrl + "/information/1");
        click(By.linkText("정보 새로고침"));
        assertThat(acceptAlert()).isEqualTo("3개의 정보를 새로고침했습니다.");
        wait.until(ExpectedConditions.urlContains("/information/1"));

        driver.get(baseUrl + "/board/content/100");
        click(By.linkText("참여하기"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("모임 나가기")));
        assertThat(driver.getPageSource()).contains("모임 나가기");

        click(By.linkText("모임 나가기"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("참여하기")));
        assertThat(driver.getPageSource()).contains("참여하기");

        driver.get(baseUrl + "/mypage");
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        passwordInput.clear();
        select(By.id("chooseRole"), "job");
        click(By.cssSelector("form.create-form button[type='submit']"));
        wait.until(ExpectedConditions.urlContains("/main"));

        driver.get(baseUrl + "/board/create");
        type(By.id("title"), boardTitle);
        type(By.id("content"), "브라우저 테스트용 모임입니다.");
        type(By.id("total"), "4");
        click(By.cssSelector("form.create-form button[type='submit']"));
        wait.until(ExpectedConditions.urlContains("/board/1"));

        wait.until(driver -> boardService.findAll().stream()
                .anyMatch(board -> boardTitle.equals(board.getTitle())));

        Long createdBoardId = boardService.findAll().stream()
                .filter(board -> boardTitle.equals(board.getTitle()))
                .map(Board::getId)
                .findFirst()
                .orElseThrow();

        driver.get(baseUrl + "/board/content/" + createdBoardId);
        click(By.linkText("모집 마감하기"));
        wait.until(driver -> boardService.findById(createdBoardId).isDeadlineScheduled());

        Board board = boardService.findById(createdBoardId);
        assertThat(board.isDeadlineScheduled()).isTrue();
        assertThat(board.isAgentFull()).isTrue();
    }

    private void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void select(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(element).selectByValue(value);
    }

    private String acceptAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }
}
