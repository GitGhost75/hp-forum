package de.csc.hpforum.survey.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SurveyStatus {
    DRAFT("draft"),
    ACTIVE("active"),
    ACTIVE_EXTENDED("active_extended"),
    ACTIVE_OVERDUE("active_overdue"),
    CLOSED("closed"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    ARCHIVED("archived");

    private final String code;

    SurveyStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static SurveyStatus fromCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        for (SurveyStatus status : values()) {
            if (status.code.equalsIgnoreCase(code.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown survey status code: " + code);
    }
}

