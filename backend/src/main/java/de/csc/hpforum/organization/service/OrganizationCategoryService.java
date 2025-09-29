package de.csc.hpforum.organization.service;

import de.csc.hpforum.organization.mapper.OrganizationCategoryMapper;
import de.csc.hpforum.organization.model.dto.OrganizationCategoryDto;
import de.csc.hpforum.organization.model.entity.OrganizationCategory;
import de.csc.hpforum.organization.repository.OrganizationCategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationCategoryService {

  private final OrganizationCategoryRepository organizationCategoryRepository;
  private final OrganizationCategoryMapper organizationCategoryMapper;

  public List<OrganizationCategoryDto> findAll() {
    return organizationCategoryMapper.toDtoList(organizationCategoryRepository.findAll());
  }

  public Optional<OrganizationCategoryDto> findById(UUID id) {
    return organizationCategoryRepository.findById(id).map(organizationCategoryMapper::toDto);
  }

  @Transactional
  public OrganizationCategoryDto create(OrganizationCategoryDto dto) {
    OrganizationCategory entity = organizationCategoryMapper.toNewEntity(dto);
    OrganizationCategory saved = organizationCategoryRepository.save(entity);
    return organizationCategoryMapper.toDto(saved);
  }

  @Transactional
  public Optional<OrganizationCategoryDto> update(UUID id, OrganizationCategoryDto dto) {
    return organizationCategoryRepository.findById(id)
        .map(existing -> {
          organizationCategoryMapper.updateEntityFromDto(dto, existing);
          OrganizationCategory saved = organizationCategoryRepository.save(existing);
          return organizationCategoryMapper.toDto(saved);
        });
  }
}
