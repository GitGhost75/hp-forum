package de.csc.hpforum.survey.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.csc.hpforum.survey.model.entity.SurveyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyDto {

    private UUID id;

    @NotBlank
    @Size(max = 64)
    private String surveyNumber;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 1024)
    private String shortDescription;

    @Size(max = 8192)
    private String longDescription;

    @NotNull
    private SurveyStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String statusDisplayText;

    private OffsetDateTime responseDeadline;

    @NotNull
    private UUID topicId;

    @NotNull
    private UUID surveyCategoryId;

    private List<UUID> organizationIds;

    private UUID decisionKeyId;

    private BigDecimal totalAmountGross;

    private Integer personDays;

    private BigDecimal dailyRateGross;

    private BigDecimal recurringCostsGrossPa;

    private OffsetDateTime activationDate;

    private OffsetDateTime closureDate;

    private OffsetDateTime archiveDate;

    private OffsetDateTime extendedDeadline;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;
}

