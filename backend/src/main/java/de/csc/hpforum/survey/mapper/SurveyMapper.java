package de.csc.hpforum.survey.mapper;

import de.csc.hpforum.survey.i18n.SurveyStatusMessageResolver;
import de.csc.hpforum.survey.model.dto.SurveyDto;
import de.csc.hpforum.survey.model.entity.Survey;
import de.csc.hpforum.survey.model.entity.SurveyCategory;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class SurveyMapper {

    @Autowired
    protected SurveyStatusMessageResolver statusMessageResolver;

    @Mapping(target = "createdAt", expression = "java(extractCreatedAt(entity))")
    @Mapping(target = "statusDisplayText", expression = "java(resolveStatusDisplayText(entity))")
    @Mapping(target = "surveyCategoryId", expression = "java(extractSurveyCategoryId(entity))")
    public abstract SurveyDto toDto(Survey entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "surveyCategory", ignore = true)
    public abstract Survey toEntity(SurveyDto dto);

    public abstract List<SurveyDto> toDtoList(List<Survey> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "surveyCategory", ignore = true)
    public abstract void updateEntityFromDto(SurveyDto dto, @MappingTarget Survey entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "surveyCategory", ignore = true)
    public abstract Survey toNewEntity(SurveyDto dto);

    protected OffsetDateTime extractCreatedAt(Survey entity) {
        return entity.getCreatedDate().orElse(null);
    }

    protected UUID extractSurveyCategoryId(Survey entity) {
        SurveyCategory category = entity.getSurveyCategory();
        return category != null ? category.getId() : null;
    }

    protected String resolveStatusDisplayText(Survey entity) {
        Locale locale = determineLocale();
        return statusMessageResolver.resolve(entity.getStatus(), locale);
    }

    private Locale determineLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale != null ? locale : Locale.getDefault();
    }
}
