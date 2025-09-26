package de.csc.hpforum.survey.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    private static final Map<SurveyStatus, Set<SurveyStatus>> TRANSITIONS;

    static {
        Map<SurveyStatus, Set<SurveyStatus>> transitions = new EnumMap<>(SurveyStatus.class);
        transitions.put(DRAFT, EnumSet.of(ACTIVE, CANCELLED));
        transitions.put(ACTIVE, EnumSet.of(ACTIVE_EXTENDED, ACTIVE_OVERDUE, CLOSED, CANCELLED));
        transitions.put(ACTIVE_EXTENDED, EnumSet.of(ACTIVE_OVERDUE, CLOSED, CANCELLED));
        transitions.put(ACTIVE_OVERDUE, EnumSet.of(CLOSED, CANCELLED));
        transitions.put(CLOSED, EnumSet.of(COMPLETED, CANCELLED, ARCHIVED));
        transitions.put(COMPLETED, EnumSet.of(ARCHIVED));
        transitions.put(CANCELLED, EnumSet.of(ARCHIVED));
        transitions.put(ARCHIVED, EnumSet.noneOf(SurveyStatus.class));
        TRANSITIONS = Collections.unmodifiableMap(transitions);
    }

    public Set<SurveyStatus> getAllowedTransitions() {
        Set<SurveyStatus> allowed = TRANSITIONS.get(this);
        return allowed != null ? Collections.unmodifiableSet(allowed) : Collections.emptySet();
    }
}

