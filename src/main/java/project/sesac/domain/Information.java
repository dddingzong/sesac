package project.sesac.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@Entity
@ToString
public class Information {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    private int infoRole; // 0이면 복지 1이면 취업

    public Information(String title, String url, int infoRole) {
        this.title = title;
        this.url = url;
        this.infoRole = infoRole;
    }
}
