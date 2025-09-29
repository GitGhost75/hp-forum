package de.csc.hpforum.common.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import de.csc.hpforum.common.model.BaseModel;

@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BaseAuditMapperConfig {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    void ignoreAuditFields(BaseModel source, @MappingTarget BaseModel target);
}
