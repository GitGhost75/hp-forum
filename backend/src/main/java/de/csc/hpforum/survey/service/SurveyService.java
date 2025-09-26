package de.csc.hpforum.survey.service;

import de.csc.hpforum.survey.mapper.SurveyMapper;
import de.csc.hpforum.survey.model.dto.SurveyDto;
import de.csc.hpforum.survey.model.entity.Survey;
import de.csc.hpforum.survey.model.entity.SurveyCategory;
import de.csc.hpforum.survey.repository.SurveyCategoryRepository;
import de.csc.hpforum.survey.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final SurveyCategoryRepository surveyCategoryRepository;
  private final SurveyMapper surveyMapper;

  public List<SurveyDto> findAll() {
    return surveyMapper.toDtoList(surveyRepository.findAll());
  }

  public Optional<SurveyDto> findById(UUID id) {
    return surveyRepository.findById(id).map(surveyMapper::toDto);
  }

  @Transactional
  public SurveyDto create(SurveyDto dto) {
    Survey entity = surveyMapper.toNewEntity(dto);
    entity.setSurveyCategory(resolveSurveyCategory(dto.getSurveyCategoryId()));
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
          Survey saved = surveyRepository.save(existing);
          return surveyMapper.toDto(saved);
        });
  }

  private SurveyCategory resolveSurveyCategory(UUID categoryId) {
    return surveyCategoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("SurveyCategory not found: " + categoryId));
  }
}
