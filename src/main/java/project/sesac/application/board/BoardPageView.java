package project.sesac.application.board;

import project.sesac.application.common.PaginationResult;
import project.sesac.domain.Board;

public record BoardPageView(PaginationResult<Board> page, String keyword) {
}
