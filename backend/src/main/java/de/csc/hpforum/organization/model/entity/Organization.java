package de.csc.hpforum.organization.model.entity;

import de.csc.hpforum.common.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

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
}
