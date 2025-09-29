package de.csc.hpforum.survey.controller;

import de.csc.hpforum.survey.model.dto.SurveyCategoryDto;
import de.csc.hpforum.survey.service.SurveyCategoryService;
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
@RequestMapping("/api/survey-categories")
@RequiredArgsConstructor
public class SurveyCategoryController {

  private final SurveyCategoryService surveyCategoryService;

  @GetMapping
  @Operation(summary = "List survey categories", description = "Returns all survey categories")
  public List<SurveyCategoryDto> findAll() {
    return surveyCategoryService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get survey category", description = "Returns the survey category for the given id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Survey category found",
          content = @Content(schema = @Schema(implementation = SurveyCategoryDto.class))),
      @ApiResponse(responseCode = "404", description = "Survey category not found")
  })
  public ResponseEntity<SurveyCategoryDto> findById(
      @Parameter(description = "Survey category id", required = true) @PathVariable UUID id) {
    return surveyCategoryService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create survey category")
  @ApiResponse(responseCode = "201", description = "Survey category created",
      content = @Content(schema = @Schema(implementation = SurveyCategoryDto.class)))
  public ResponseEntity<SurveyCategoryDto> create(@Valid @RequestBody SurveyCategoryDto dto) {
    SurveyCategoryDto created = surveyCategoryService.create(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update survey category")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Survey category updated",
          content = @Content(schema = @Schema(implementation = SurveyCategoryDto.class))),
      @ApiResponse(responseCode = "404", description = "Survey category not found")
  })
  public ResponseEntity<SurveyCategoryDto> update(
      @Parameter(description = "Survey category id", required = true) @PathVariable UUID id,
      @Valid @RequestBody SurveyCategoryDto dto) {
    return surveyCategoryService.update(id, dto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}
