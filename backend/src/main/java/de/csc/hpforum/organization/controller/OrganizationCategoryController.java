package de.csc.hpforum.organization.controller;

import de.csc.hpforum.organization.model.dto.OrganizationCategoryDto;
import de.csc.hpforum.organization.service.OrganizationCategoryService;
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
@RequestMapping("/api/organization-categories")
@RequiredArgsConstructor
public class OrganizationCategoryController {

  private final OrganizationCategoryService organizationCategoryService;

  @GetMapping
  @Operation(summary = "List organization categories", description = "Returns all organization categories")
  public List<OrganizationCategoryDto> findAll() {
    return organizationCategoryService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get organization category", description = "Returns the organization category for the given id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Organization category found",
          content = @Content(schema = @Schema(implementation = OrganizationCategoryDto.class))),
      @ApiResponse(responseCode = "404", description = "Organization category not found")
  })
  public ResponseEntity<OrganizationCategoryDto> findById(
      @Parameter(description = "Organization category id", required = true) @PathVariable UUID id) {
    return organizationCategoryService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create organization category")
  @ApiResponse(responseCode = "201", description = "Organization category created",
      content = @Content(schema = @Schema(implementation = OrganizationCategoryDto.class)))
  public ResponseEntity<OrganizationCategoryDto> create(@Valid @RequestBody OrganizationCategoryDto dto) {
    OrganizationCategoryDto created = organizationCategoryService.create(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update organization category")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Organization category updated",
          content = @Content(schema = @Schema(implementation = OrganizationCategoryDto.class))),
      @ApiResponse(responseCode = "404", description = "Organization category not found")
  })
  public ResponseEntity<OrganizationCategoryDto> update(
      @Parameter(description = "Organization category id", required = true) @PathVariable UUID id,
      @Valid @RequestBody OrganizationCategoryDto dto) {
    return organizationCategoryService.update(id, dto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}
