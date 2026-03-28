package project.sesac.application.information;

import project.sesac.application.common.PaginationResult;
import project.sesac.domain.Information;

public record InformationPageView(PaginationResult<Information> page, Integer role, String keyword) {
}
