package de.csc.hpforum.user.model.entity;

import de.csc.hpforum.common.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "role")
public class Role extends BaseModel {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;

    @NotAudited
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
}
