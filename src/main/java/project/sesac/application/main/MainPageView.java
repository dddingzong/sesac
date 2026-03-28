package project.sesac.application.main;

import project.sesac.application.common.ProfileLevel;
import project.sesac.domain.Board;
import project.sesac.domain.Information;

import java.util.List;

public record MainPageView(
        String name,
        ProfileLevel profileLevel,
        boolean clearMission1,
        boolean clearMission2,
        String mission1,
        String mission2,
        List<Information> informations,
        List<Board> boards
) {
}
