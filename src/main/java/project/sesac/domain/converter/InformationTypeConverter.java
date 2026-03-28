package project.sesac.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import project.sesac.domain.type.InformationType;

@Converter(autoApply = false)
public class InformationTypeConverter implements AttributeConverter<InformationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InformationType attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public InformationType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : InformationType.fromCode(dbData);
    }
}
