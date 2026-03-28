package project.sesac.application.board;

import project.sesac.domain.Agent;
import project.sesac.domain.Board;

import java.util.List;

public record BoardDetailView(Board board, String loginId, List<Agent> agentList, List<String> participantLoginIds) {
}
