package de.csc.hpforum.survey.model.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;

@Converter(autoApply = true)
public class SurveyStatusConverter implements AttributeConverter<SurveyStatus, String> {

    @Override
    public String convertToDatabaseColumn(SurveyStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public SurveyStatus convertToEntityAttribute(String dbData) {
        return SurveyStatus.fromJson(dbData);
    }
}

