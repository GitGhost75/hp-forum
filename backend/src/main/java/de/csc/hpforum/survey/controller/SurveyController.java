package de.csc.hpforum.survey.controller;

import de.csc.hpforum.survey.model.dto.SurveyDto;
import de.csc.hpforum.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  private final SurveyService surveyService;

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
}
