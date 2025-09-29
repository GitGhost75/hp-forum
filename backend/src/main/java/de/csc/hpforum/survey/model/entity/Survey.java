package de.csc.hpforum.survey.model.entity;

import de.csc.hpforum.common.model.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "survey")
public class Survey extends BaseModel {

    @Column(name = "survey_number", nullable = false, unique = true, length = 64)
    private String surveyNumber;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "short_description", nullable = false, length = 1024)
    private String shortDescription;

    @Column(name = "long_description", length = 8192)
    private String longDescription;

    @Column(name = "status", nullable = false, length = 32)
    private SurveyStatus status;

    @Column(name = "response_deadline")
    private OffsetDateTime responseDeadline;

    @Column(name = "topic_id", nullable = false, columnDefinition = "uuid")
    private UUID topicId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_category_id", nullable = false)
    private SurveyCategory surveyCategory;

    @Column(name = "decision_key_id", columnDefinition = "uuid")
    private UUID decisionKeyId;

    @Column(name = "total_amount_gross", precision = 15, scale = 2)
    private BigDecimal totalAmountGross;

    @Column(name = "person_days")
    private Integer personDays;

    @Column(name = "daily_rate_gross", precision = 12, scale = 2)
    private BigDecimal dailyRateGross;

    @Column(name = "recurring_costs_gross_pa", precision = 15, scale = 2)
    private BigDecimal recurringCostsGrossPa;

    @Column(name = "activation_date")
    private OffsetDateTime activationDate;

    @Column(name = "closure_date")
    private OffsetDateTime closureDate;

    @Column(name = "archive_date")
    private OffsetDateTime archiveDate;

    @Column(name = "extended_deadline")
    private OffsetDateTime extendedDeadline;

    @NotAudited
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SurveyOrganization> surveyOrganizations = new HashSet<>();

    public void addSurveyOrganization(SurveyOrganization surveyOrganization) {
        surveyOrganizations.add(surveyOrganization);
        surveyOrganization.setSurvey(this);
    }

    public void clearSurveyOrganizations() {
        if (surveyOrganizations == null) {
            return;
        }
        surveyOrganizations.forEach(link -> link.setSurvey(null));
        surveyOrganizations.clear();
    }
}
