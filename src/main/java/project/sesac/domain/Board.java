package project.sesac.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class Board {

    @Id @GeneratedValue
    private Long id;

    private String author; // main_id

    private String title;

    private String content;

    private int total; // 총인원 (최대인원)

    private int agent; // 모집인원 (모인인원)
}
