package de.csc.hpforum.survey.mapper;

import de.csc.hpforum.common.model.AuditUser;
import de.csc.hpforum.survey.model.dto.SurveyDto;
import de.csc.hpforum.survey.model.entity.Survey;
import de.csc.hpforum.survey.model.entity.SurveyCategory;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SurveyMapper {

    // @Mapping(target = "createdById", expression = "java(extractCreatedById(entity))")
    @Mapping(target = "createdAt", expression = "java(extractCreatedAt(entity))")
    @Mapping(target = "surveyCategoryId", expression = "java(extractSurveyCategoryId(entity))")
    SurveyDto toDto(Survey entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "surveyCategory", ignore = true)
    Survey toEntity(SurveyDto dto);

    List<SurveyDto> toDtoList(List<Survey> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "surveyCategory", ignore = true)
    void updateEntityFromDto(SurveyDto dto, @MappingTarget Survey entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "surveyCategory", ignore = true)
    Survey toNewEntity(SurveyDto dto);

    default UUID extractCreatedById(Survey entity) {
        return entity.getCreatedBy().map(AuditUser::getUserId).orElse(null);
    }

    default OffsetDateTime extractCreatedAt(Survey entity) {
        return entity.getCreatedDate().orElse(null);
    }

    default UUID extractSurveyCategoryId(Survey entity) {
        SurveyCategory category = entity.getSurveyCategory();
        return category != null ? category.getId() : null;
    }
}
