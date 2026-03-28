package project.sesac.domain;

import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.sesac.domain.converter.MissionTypeConverter;
import project.sesac.domain.type.MissionType;

@Getter
@RequiredArgsConstructor
@Entity
public class Mission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "content_role")
    @Convert(converter = MissionTypeConverter.class)
    private MissionType missionType;

    public Mission(String content, MissionType missionType) {
        this.content = content;
        this.missionType = missionType;
    }

    public int getContentRole() {
        return missionType.code();
    }

}
