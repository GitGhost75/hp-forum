package com.example.hpforum.survey.api;

import com.example.hpforum.survey.api.dto.SurveyDto;
import com.example.hpforum.survey.model.Survey;
import com.example.hpforum.survey.mapper.SurveyMapper;
import com.example.hpforum.survey.repository.SurveyRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;

    @GetMapping
    public List<SurveyDto> findAll() {
        return surveyMapper.toDtoList(surveyRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> findById(@PathVariable UUID id) {
        return surveyRepository.findById(id)
            .map(surveyMapper::toDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SurveyDto> create(@Valid @RequestBody SurveyDto dto) {
        Survey toPersist = surveyMapper.toNewEntity(dto);
        Survey saved = surveyRepository.save(toPersist);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();
        return ResponseEntity.created(location).body(surveyMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDto> update(@PathVariable UUID id, @Valid @RequestBody SurveyDto dto) {
        return surveyRepository.findById(id)
            .map(existing -> {
                surveyMapper.updateEntityFromDto(dto, existing);
                existing.setId(id);
                Survey saved = surveyRepository.save(existing);
                return ResponseEntity.ok(surveyMapper.toDto(saved));
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
