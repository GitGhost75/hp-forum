package de.csc.hpforum.survey.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

public enum SurveyStatus {
    DRAFT,
    ACTIVE,
    ACTIVE_EXTENDED,
    ACTIVE_OVERDUE,
    CLOSED,
    COMPLETED,
    CANCELLED,
    ARCHIVED;

    @JsonValue
    public String toJson() {
        return name().toLowerCase(Locale.ROOT);
    }

    @JsonCreator
    public static SurveyStatus fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return SurveyStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}

