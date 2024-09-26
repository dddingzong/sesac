package project.sesac.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class Mission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int contentRole; // 0이면 기본, 1이면 야외, 2이면 만남

    public Mission(String content, int contentRole) {
        this.content = content;
        this.contentRole = contentRole;
    }

}
