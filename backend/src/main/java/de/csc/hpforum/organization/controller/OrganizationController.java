package de.csc.hpforum.organization.controller;

import de.csc.hpforum.organization.model.dto.OrganizationDto;
import de.csc.hpforum.organization.service.OrganizationService;
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
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;

  @GetMapping
  @Operation(summary = "List organizations", description = "Returns all organizations")
  public List<OrganizationDto> findAll() {
    return organizationService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get organization", description = "Returns the organization for the given id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Organization found",
          content = @Content(schema = @Schema(implementation = OrganizationDto.class))),
      @ApiResponse(responseCode = "404", description = "Organization not found")
  })
  public ResponseEntity<OrganizationDto> findById(
      @Parameter(description = "Organization id", required = true) @PathVariable UUID id) {
    return organizationService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create organization")
  @ApiResponse(responseCode = "201", description = "Organization created",
      content = @Content(schema = @Schema(implementation = OrganizationDto.class)))
  public ResponseEntity<OrganizationDto> create(@Valid @RequestBody OrganizationDto dto) {
    OrganizationDto created = organizationService.create(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update organization")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Organization updated",
          content = @Content(schema = @Schema(implementation = OrganizationDto.class))),
      @ApiResponse(responseCode = "404", description = "Organization not found")
  })
  public ResponseEntity<OrganizationDto> update(
      @Parameter(description = "Organization id", required = true) @PathVariable UUID id,
      @Valid @RequestBody OrganizationDto dto) {
    return organizationService.update(id, dto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}
