package de.csc.hpforum.common.model;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel implements Auditable<AuditUser, UUID, OffsetDateTime> {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "created_by_user_id")),
        @AttributeOverride(name = "username", column = @Column(name = "created_by_username"))
    })
    @Embedded
    @CreatedBy
    private AuditUser createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private OffsetDateTime createdDate;

    @AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "last_modified_by_user_id")),
        @AttributeOverride(name = "username", column = @Column(name = "last_modified_by_username"))
    })
    @Embedded
    @LastModifiedBy
    private AuditUser lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private OffsetDateTime lastModifiedDate;

    @Version
    @Column(name = "version")
    private Long version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isNew() {
        return version == null;
    }

    public Optional<AuditUser> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public Optional<OffsetDateTime> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public Optional<AuditUser> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public Optional<OffsetDateTime> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public void setCreatedBy(AuditUser createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(OffsetDateTime creationDate) {
        this.createdDate = creationDate;
    }

    public void setLastModifiedBy(AuditUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
