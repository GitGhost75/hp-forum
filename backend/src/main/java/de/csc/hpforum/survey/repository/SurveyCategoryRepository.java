package de.csc.hpforum.survey.repository;

import de.csc.hpforum.survey.model.entity.SurveyCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyCategoryRepository extends JpaRepository<SurveyCategory, UUID> {
}
