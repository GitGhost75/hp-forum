package de.csc.hpforum.survey.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.csc.hpforum.survey.model.entity.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}

