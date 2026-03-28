package project.sesac.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import project.sesac.domain.type.MissionStage;

@Converter(autoApply = false)
public class MissionStageConverter implements AttributeConverter<MissionStage, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MissionStage attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public MissionStage convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : MissionStage.fromCode(dbData);
    }
}
