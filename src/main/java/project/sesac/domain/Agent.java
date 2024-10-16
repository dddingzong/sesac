package project.sesac.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@Entity
@ToString
public class Agent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long board_id;

    private String agent;

    public Agent(Long board_id, String agent) {
        this.board_id = board_id;
        this.agent = agent;
    }
}
