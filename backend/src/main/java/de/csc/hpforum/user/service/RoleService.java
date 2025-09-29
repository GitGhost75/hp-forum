package de.csc.hpforum.user.service;

import de.csc.hpforum.user.mapper.RoleMapper;
import de.csc.hpforum.user.model.dto.RoleDto;
import de.csc.hpforum.user.model.entity.Role;
import de.csc.hpforum.user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  public List<RoleDto> findAll() {
    return roleMapper.toDtoList(roleRepository.findAll());
  }

  public Optional<RoleDto> findById(UUID id) {
    return roleRepository.findById(id).map(roleMapper::toDto);
  }

  @Transactional
  public RoleDto create(RoleDto dto) {
    Role entity = roleMapper.toNewEntity(dto);
    Role saved = roleRepository.save(entity);
    return roleMapper.toDto(saved);
  }

  @Transactional
  public Optional<RoleDto> update(UUID id, RoleDto dto) {
    return roleRepository.findById(id)
        .map(existing -> {
          roleMapper.updateEntityFromDto(dto, existing);
          Role saved = roleRepository.save(existing);
          return roleMapper.toDto(saved);
        });
  }
}
