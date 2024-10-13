package project.sesac.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDto {

    private String title;
    private String author;
    private String content;
    private String total;

    public BoardDto(String title, String author, String content, String total) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.total = total;
    }
}
