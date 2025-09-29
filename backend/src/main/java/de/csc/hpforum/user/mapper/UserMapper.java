package de.csc.hpforum.user.mapper;

import de.csc.hpforum.common.mapper.BaseAuditMapperConfig;
import de.csc.hpforum.user.model.dto.UserDto;
import de.csc.hpforum.user.model.entity.Role;
import de.csc.hpforum.user.model.entity.User;
import java.util.List;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), config = BaseAuditMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "organizationId", expression = "java(extractOrganizationId(entity))")
    @Mapping(target = "roleId", expression = "java(extractRoleId(entity))")
    UserDto toDto(User entity);

    List<UserDto> toDtoList(List<User> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toNewEntity(UserDto dto);

    default UUID extractOrganizationId(User entity) {
        return entity.getOrganization() != null ? entity.getOrganization().getId() : null;
    }

    default UUID extractRoleId(User entity) {
        Role role = entity.getRole();
        return role != null ? role.getId() : null;
    }
}
