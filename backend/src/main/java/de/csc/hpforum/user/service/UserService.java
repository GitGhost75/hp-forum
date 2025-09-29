package de.csc.hpforum.user.service;

import de.csc.hpforum.organization.model.entity.Organization;
import de.csc.hpforum.organization.repository.OrganizationRepository;
import de.csc.hpforum.user.mapper.UserMapper;
import de.csc.hpforum.user.model.dto.UserDto;
import de.csc.hpforum.user.model.entity.Role;
import de.csc.hpforum.user.model.entity.User;
import de.csc.hpforum.user.repository.RoleRepository;
import de.csc.hpforum.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final OrganizationRepository organizationRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;

  public List<UserDto> findAll() {
    return userMapper.toDtoList(userRepository.findAll());
  }

  public Optional<UserDto> findById(UUID id) {
    return userRepository.findById(id).map(userMapper::toDto);
  }

  @Transactional
  public UserDto create(UserDto dto) {
    User entity = userMapper.toNewEntity(dto);
    entity.setOrganization(resolveOrganization(dto.getOrganizationId()));
    entity.setRole(resolveRole(dto.getRoleId()));
    User saved = userRepository.save(entity);
    return userMapper.toDto(saved);
  }

  @Transactional
  public Optional<UserDto> update(UUID id, UserDto dto) {
    return userRepository.findById(id)
        .map(existing -> {
          userMapper.updateEntityFromDto(dto, existing);
          if (dto.getOrganizationId() != null) {
            Organization currentOrg = existing.getOrganization();
            if (currentOrg == null || !dto.getOrganizationId().equals(currentOrg.getId())) {
              existing.setOrganization(resolveOrganization(dto.getOrganizationId()));
            }
          }
          if (dto.getRoleId() != null) {
            Role currentRole = existing.getRole();
            if (currentRole == null || !dto.getRoleId().equals(currentRole.getId())) {
              existing.setRole(resolveRole(dto.getRoleId()));
            }
          }
          User saved = userRepository.save(existing);
          return userMapper.toDto(saved);
        });
  }

  private Organization resolveOrganization(UUID organizationId) {
    return organizationRepository.findById(organizationId)
        .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + organizationId));
  }

  private Role resolveRole(UUID roleId) {
    return roleRepository.findById(roleId)
        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));
  }
}
