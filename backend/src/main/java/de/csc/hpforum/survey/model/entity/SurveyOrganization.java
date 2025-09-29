package de.csc.hpforum.survey.model.entity;

import de.csc.hpforum.common.model.BaseModel;
import de.csc.hpforum.organization.model.entity.Organization;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "survey_organization")
public class SurveyOrganization extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public SurveyOrganization(Survey survey, Organization organization) {
        this.survey = survey;
        this.organization = organization;
    }

}
