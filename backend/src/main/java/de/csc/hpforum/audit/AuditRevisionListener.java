package de.csc.hpforum.audit;

import java.util.Optional;
import org.hibernate.envers.RevisionListener;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity rev = (AuditRevisionEntity) revisionEntity;
        String username = Optional.ofNullable(System.getProperty("user.name"))
            .filter(name -> !name.isBlank())
            .orElse("system");
        rev.setUsername(username);
    }
}

