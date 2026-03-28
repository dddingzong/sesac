package project.sesac.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import project.sesac.domain.type.MissionType;

@Converter(autoApply = false)
public class MissionTypeConverter implements AttributeConverter<MissionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MissionType attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public MissionType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : MissionType.fromCode(dbData);
    }
}
