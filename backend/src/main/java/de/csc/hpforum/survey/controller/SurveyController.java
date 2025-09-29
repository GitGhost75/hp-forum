package de.csc.hpforum.survey.controller;

import de.csc.hpforum.survey.i18n.SurveyStatusMessageResolver;
import de.csc.hpforum.survey.model.dto.SurveyDto;
import de.csc.hpforum.survey.model.dto.SurveyStatusOptionDto;
import de.csc.hpforum.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;
  private final SurveyStatusMessageResolver statusMessageResolver;

  @GetMapping
  @Operation(summary = "List surveys", description = "Returns all surveys")
  public List<SurveyDto> findAll() {
    return surveyService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get survey", description = "Returns the survey for the given id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Survey found",
          content = @Content(schema = @Schema(implementation = SurveyDto.class))),
      @ApiResponse(responseCode = "404", description = "Survey not found")
  })
  public ResponseEntity<SurveyDto> findById(
      @Parameter(description = "Survey id", required = true) @PathVariable UUID id) {
    return surveyService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create survey")
  @ApiResponse(responseCode = "201", description = "Survey created",
      content = @Content(schema = @Schema(implementation = SurveyDto.class)))
  public ResponseEntity<SurveyDto> create(@Valid @RequestBody SurveyDto dto) {
    SurveyDto created = surveyService.create(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update survey")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Survey updated",
          content = @Content(schema = @Schema(implementation = SurveyDto.class))),
      @ApiResponse(responseCode = "404", description = "Survey not found")
  })
  public ResponseEntity<SurveyDto> update(
      @Parameter(description = "Survey id", required = true) @PathVariable UUID id,
      @Valid @RequestBody SurveyDto dto) {
    return surveyService.update(id, dto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @GetMapping("/{id}/status-transitions")
  @Operation(summary = "Get allowed status transitions",
      description = "Returns the list of statuses the survey can transition to from its current state")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Transitions returned",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = SurveyStatusOptionDto.class)))),
      @ApiResponse(responseCode = "404", description = "Survey not found")
  })
  public ResponseEntity<List<SurveyStatusOptionDto>> getStatusTransitions(
      @Parameter(description = "Survey id", required = true) @PathVariable UUID id,
      Locale locale) {
    Locale effectiveLocale = locale != null ? locale : LocaleContextHolder.getLocale();
    try {
      List<SurveyStatusOptionDto> options = surveyService.getAllowedTransitions(id).stream()
          .map(status -> SurveyStatusOptionDto.builder()
              .status(status)
              .displayText(statusMessageResolver.resolve(status, effectiveLocale))
              .build())
          .toList();
      return ResponseEntity.ok(options);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
