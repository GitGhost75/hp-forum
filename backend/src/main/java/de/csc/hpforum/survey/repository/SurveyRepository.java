package de.csc.hpforum.survey.repository;

import de.csc.hpforum.survey.model.Survey;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}

