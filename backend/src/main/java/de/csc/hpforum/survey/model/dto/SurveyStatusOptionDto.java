package de.csc.hpforum.survey.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.csc.hpforum.survey.model.entity.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyStatusOptionDto {

    private SurveyStatus status;
    private String displayText;
}
