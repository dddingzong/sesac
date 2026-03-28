package project.sesac.domain;

import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import project.sesac.domain.converter.InformationTypeConverter;
import project.sesac.domain.type.InformationPreference;
import project.sesac.domain.type.InformationType;

@Getter
@RequiredArgsConstructor
@Entity
@ToString
public class Information {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    @Column(name = "info_role")
    @Convert(converter = InformationTypeConverter.class)
    private InformationType informationType;

    public Information(String title, String url, InformationType informationType) {
        this.title = title;
        this.url = url;
        this.informationType = informationType;
    }

    public boolean matches(InformationPreference preference) {
        return preference.includes(informationType);
    }

    public int getInfoRole() {
        return informationType.code();
    }

    public String getInfoRoleLabel() {
        return informationType.label();
    }
}
