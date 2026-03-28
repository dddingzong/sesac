package project.sesac.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import project.sesac.domain.type.InformationPreference;

@Converter(autoApply = false)
public class InformationPreferenceConverter implements AttributeConverter<InformationPreference, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InformationPreference attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public InformationPreference convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : InformationPreference.fromCode(dbData);
    }
}
