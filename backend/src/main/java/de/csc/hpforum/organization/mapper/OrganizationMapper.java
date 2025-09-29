package de.csc.hpforum.organization.mapper;

import de.csc.hpforum.common.mapper.BaseAuditMapperConfig;
import de.csc.hpforum.organization.model.dto.OrganizationDto;
import de.csc.hpforum.organization.model.entity.Organization;
import de.csc.hpforum.organization.model.entity.OrganizationCategory;
import java.util.List;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), config = BaseAuditMapperConfig.class)
public interface OrganizationMapper {

    @Mapping(target = "organizationCategoryId", expression = "java(extractOrganizationCategoryId(entity))")
    OrganizationDto toDto(Organization entity);

    List<OrganizationDto> toDtoList(List<Organization> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationCategory", ignore = true)
    Organization toEntity(OrganizationDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "organizationCategory", ignore = true)
    void updateEntityFromDto(OrganizationDto dto, @MappingTarget Organization entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationCategory", ignore = true)
    Organization toNewEntity(OrganizationDto dto);

    default UUID extractOrganizationCategoryId(Organization entity) {
        OrganizationCategory category = entity.getOrganizationCategory();
        return category != null ? category.getId() : null;
    }
}
