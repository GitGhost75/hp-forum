package de.csc.hpforum.survey.service;

import de.csc.hpforum.survey.mapper.SurveyCategoryMapper;
import de.csc.hpforum.survey.model.dto.SurveyCategoryDto;
import de.csc.hpforum.survey.model.entity.SurveyCategory;
import de.csc.hpforum.survey.repository.SurveyCategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyCategoryService {

  private final SurveyCategoryRepository surveyCategoryRepository;
  private final SurveyCategoryMapper surveyCategoryMapper;

  public List<SurveyCategoryDto> findAll() {
    return surveyCategoryMapper.toDtoList(surveyCategoryRepository.findAll());
  }

  public Optional<SurveyCategoryDto> findById(UUID id) {
    return surveyCategoryRepository.findById(id).map(surveyCategoryMapper::toDto);
  }

  @Transactional
  public SurveyCategoryDto create(SurveyCategoryDto dto) {
    SurveyCategory entity = surveyCategoryMapper.toNewEntity(dto);
    SurveyCategory saved = surveyCategoryRepository.save(entity);
    return surveyCategoryMapper.toDto(saved);
  }

  @Transactional
  public Optional<SurveyCategoryDto> update(UUID id, SurveyCategoryDto dto) {
    return surveyCategoryRepository.findById(id)
        .map(existing -> {
          surveyCategoryMapper.updateEntityFromDto(dto, existing);
          SurveyCategory saved = surveyCategoryRepository.save(existing);
          return surveyCategoryMapper.toDto(saved);
        });
  }
}
