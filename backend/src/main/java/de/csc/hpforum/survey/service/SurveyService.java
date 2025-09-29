package de.csc.hpforum.survey.service;

import de.csc.hpforum.organization.model.entity.Organization;
import de.csc.hpforum.organization.repository.OrganizationRepository;
import de.csc.hpforum.survey.mapper.SurveyMapper;
import de.csc.hpforum.survey.model.dto.SurveyDto;
import de.csc.hpforum.survey.model.entity.Survey;
import de.csc.hpforum.survey.model.entity.SurveyCategory;
import de.csc.hpforum.survey.model.entity.SurveyOrganization;
import de.csc.hpforum.survey.model.entity.SurveyStatus;
import de.csc.hpforum.survey.repository.SurveyCategoryRepository;
import de.csc.hpforum.survey.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final SurveyCategoryRepository surveyCategoryRepository;
  private final OrganizationRepository organizationRepository;
  private final SurveyMapper surveyMapper;

  @Transactional
  public List<SurveyDto> findAll() {
    return surveyMapper.toDtoList(surveyRepository.findAll());
  }

  @Transactional
  public Optional<SurveyDto> findById(UUID id) {
    return surveyRepository.findById(id).map(surveyMapper::toDto);
  }

  @Transactional
  public SurveyDto create(SurveyDto dto) {
    Survey entity = surveyMapper.toNewEntity(dto);
    entity.setSurveyCategory(resolveSurveyCategory(dto.getSurveyCategoryId()));
    applyOrganizations(entity, dto.getOrganizationIds());
    Survey saved = surveyRepository.save(entity);
    return surveyMapper.toDto(saved);
  }

  @Transactional
  public Optional<SurveyDto> update(UUID id, SurveyDto dto) {
    return surveyRepository.findById(id)
        .map(existing -> {
          surveyMapper.updateEntityFromDto(dto, existing);
          UUID categoryId = dto.getSurveyCategoryId();
          if (categoryId != null) {
            SurveyCategory current = existing.getSurveyCategory();
            if (current == null || !categoryId.equals(current.getId())) {
              existing.setSurveyCategory(resolveSurveyCategory(categoryId));
            }
          }
          applyOrganizations(existing, dto.getOrganizationIds());
          Survey saved = surveyRepository.save(existing);
          return surveyMapper.toDto(saved);
        });
  }

  private SurveyCategory resolveSurveyCategory(UUID categoryId) {
    return surveyCategoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("SurveyCategory not found: " + categoryId));
  }

  public Set<SurveyStatus> getAllowedTransitions(UUID surveyId) {
    Survey survey = surveyRepository.findById(surveyId)
        .orElseThrow(() -> new EntityNotFoundException("Survey not found: " + surveyId));
    return survey.getStatus().getAllowedTransitions();
  }

  private void applyOrganizations(Survey survey, List<UUID> organizationIds) {
    survey.clearSurveyOrganizations();
    if (organizationIds == null || organizationIds.isEmpty()) {
      return;
    }
    organizationIds.stream()
        .filter(Objects::nonNull)
        .distinct()
        .map(this::resolveOrganization)
        .forEach(org -> survey.addSurveyOrganization(new SurveyOrganization(survey, org)));
  }

  private Organization resolveOrganization(UUID organizationId) {
    return organizationRepository.findById(organizationId)
        .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + organizationId));
  }
}
