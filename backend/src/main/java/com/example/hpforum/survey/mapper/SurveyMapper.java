package com.example.hpforum.survey.mapper;

import com.example.hpforum.survey.api.dto.SurveyDto;
import com.example.hpforum.survey.model.Survey;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SurveyMapper {

    SurveyDto toDto(Survey entity);

    Survey toEntity(SurveyDto dto);

    List<SurveyDto> toDtoList(List<Survey> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(SurveyDto dto, @MappingTarget Survey entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Survey toNewEntity(SurveyDto dto);
}

