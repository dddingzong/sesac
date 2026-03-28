package project.sesac.application.main;

public record MyPageView(
        String loginId,
        String name,
        String level,
        int chooseRole,
        Long boardId,
        String boardTitle
) {
}
