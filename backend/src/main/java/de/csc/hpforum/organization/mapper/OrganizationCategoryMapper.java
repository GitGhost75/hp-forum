package de.csc.hpforum.organization.mapper;

import de.csc.hpforum.common.mapper.BaseAuditMapperConfig;
import de.csc.hpforum.organization.model.dto.OrganizationCategoryDto;
import de.csc.hpforum.organization.model.entity.OrganizationCategory;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), config = BaseAuditMapperConfig.class)
public interface OrganizationCategoryMapper {

    OrganizationCategoryDto toDto(OrganizationCategory entity);

    List<OrganizationCategoryDto> toDtoList(List<OrganizationCategory> entities);

    @Mapping(target = "id", ignore = true)
    OrganizationCategory toEntity(OrganizationCategoryDto dto);

    @Mapping(target = "id", ignore = true)
    OrganizationCategory toNewEntity(OrganizationCategoryDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(OrganizationCategoryDto dto, @MappingTarget OrganizationCategory entity);
}
