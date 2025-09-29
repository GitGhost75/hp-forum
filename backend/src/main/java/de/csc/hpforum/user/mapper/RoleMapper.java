package de.csc.hpforum.user.mapper;

import de.csc.hpforum.common.mapper.BaseAuditMapperConfig;
import de.csc.hpforum.user.model.dto.RoleDto;
import de.csc.hpforum.user.model.entity.Role;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), config = BaseAuditMapperConfig.class)
public interface RoleMapper {

    RoleDto toDto(Role entity);

    List<RoleDto> toDtoList(List<Role> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toNewEntity(RoleDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "users", ignore = true)
    void updateEntityFromDto(RoleDto dto, @MappingTarget Role entity);
}
