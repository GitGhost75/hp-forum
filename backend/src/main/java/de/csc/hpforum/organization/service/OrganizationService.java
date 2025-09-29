package de.csc.hpforum.organization.service;

import de.csc.hpforum.organization.mapper.OrganizationMapper;
import de.csc.hpforum.organization.model.dto.OrganizationDto;
import de.csc.hpforum.organization.model.entity.Organization;
import de.csc.hpforum.organization.model.entity.OrganizationCategory;
import de.csc.hpforum.organization.repository.OrganizationCategoryRepository;
import de.csc.hpforum.organization.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final OrganizationCategoryRepository organizationCategoryRepository;
  private final OrganizationMapper organizationMapper;

  public List<OrganizationDto> findAll() {
    return organizationMapper.toDtoList(organizationRepository.findAll());
  }

  public Optional<OrganizationDto> findById(UUID id) {
    return organizationRepository.findById(id).map(organizationMapper::toDto);
  }

  @Transactional
  public OrganizationDto create(OrganizationDto dto) {
    Organization entity = organizationMapper.toNewEntity(dto);
    entity.setOrganizationCategory(resolveOrganizationCategory(dto.getOrganizationCategoryId()));
    Organization saved = organizationRepository.save(entity);
    return organizationMapper.toDto(saved);
  }

  @Transactional
  public Optional<OrganizationDto> update(UUID id, OrganizationDto dto) {
    return organizationRepository.findById(id)
        .map(existing -> {
          organizationMapper.updateEntityFromDto(dto, existing);
          UUID categoryId = dto.getOrganizationCategoryId();
          if (categoryId != null) {
            OrganizationCategory current = existing.getOrganizationCategory();
            if (current == null || !categoryId.equals(current.getId())) {
              existing.setOrganizationCategory(resolveOrganizationCategory(categoryId));
            }
          }
          Organization saved = organizationRepository.save(existing);
          return organizationMapper.toDto(saved);
        });
  }

  private OrganizationCategory resolveOrganizationCategory(UUID categoryId) {
    return organizationCategoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("OrganizationCategory not found: " + categoryId));
  }
}
