package de.csc.hpforum.survey.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SurveyStatusConverter implements AttributeConverter<SurveyStatus, String> {

    @Override
    public String convertToDatabaseColumn(SurveyStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public SurveyStatus convertToEntityAttribute(String dbData) {
        return SurveyStatus.fromCode(dbData);
    }
}

