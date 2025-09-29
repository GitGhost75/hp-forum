package de.csc.hpforum.organization.model.entity;

import de.csc.hpforum.common.model.BaseModel;
import de.csc.hpforum.survey.model.entity.SurveyOrganization;
import de.csc.hpforum.user.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "organization")
public class Organization extends BaseModel {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "short_name", nullable = false, length = 64)
    private String shortName;

    @Column(name = "acn_client_number", nullable = false, length = 64)
    private String acnClientNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_category_id", nullable = false)
    private OrganizationCategory organizationCategory;

    @NotAudited
    @OneToMany(mappedBy = "organization")
    private Set<SurveyOrganization> surveyOrganizations = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "organization")
    private Set<User> users = new HashSet<>();
}
