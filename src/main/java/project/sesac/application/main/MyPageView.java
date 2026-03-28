package project.sesac.application.main;

public record MyPageView(
        String loginId,
        String loginPassword,
        String name,
        String level,
        int chooseRole,
        Long boardId,
        String boardTitle
) {
}
