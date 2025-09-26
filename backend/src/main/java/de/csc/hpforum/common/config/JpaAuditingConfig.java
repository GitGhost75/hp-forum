package de.csc.hpforum.common.config;

import de.csc.hpforum.common.model.AuditUser;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.auditing.DateTimeProvider;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditAware", dateTimeProviderRef = "utcOffsetDateTimeProvider")
public class JpaAuditingConfig {

  @Bean
  public AuditorAware<AuditUser> auditAware() {
    return () -> Optional.of(defaultAuditUser());
  }

  @Bean
  public DateTimeProvider utcOffsetDateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
  }

  private AuditUser defaultAuditUser() {
    AuditUser auditUser = new AuditUser();
    String username = Optional.ofNullable(System.getProperty("user.name"))
        .filter(name -> !name.isBlank())
        .orElse("system");
    auditUser.setUsername(username);
    auditUser.setUserId(UUID.nameUUIDFromBytes(("system:" + username).getBytes(StandardCharsets.UTF_8)));
    return auditUser;
  }
}
