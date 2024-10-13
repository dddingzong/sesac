package project.sesac.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
@ToString
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String author; // main_id

    private String title;

    private String content;

    private int total; // 총인원 (최대인원)

    private int agent; // 모집인원 (모인인원)

    private boolean agentFull; // 다 찼으면 true 다 안찼으면 false (default = false)

    public Board(String author, String title, String content, int total, int agent, boolean agentFull) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.total = total;
        this.agent = agent;
        this.agentFull = agentFull;
    }

    public void plusAgent(){
        this.agent += 1;
    }

    public void minusAgent(){
        this.agent -= 1;
    }
}
