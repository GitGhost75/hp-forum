package de.csc.hpforum.user.controller;

import de.csc.hpforum.user.model.dto.RoleDto;
import de.csc.hpforum.user.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

  private final RoleService roleService;

  @GetMapping
  @Operation(summary = "List roles", description = "Returns all roles")
  public List<RoleDto> findAll() {
    return roleService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get role", description = "Returns the role for the given id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Role found",
          content = @Content(schema = @Schema(implementation = RoleDto.class))),
      @ApiResponse(responseCode = "404", description = "Role not found")
  })
  public ResponseEntity<RoleDto> findById(
      @Parameter(description = "Role id", required = true) @PathVariable UUID id) {
    return roleService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create role")
  @ApiResponse(responseCode = "201", description = "Role created",
      content = @Content(schema = @Schema(implementation = RoleDto.class)))
  public ResponseEntity<RoleDto> create(@Valid @RequestBody RoleDto dto) {
    RoleDto created = roleService.create(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update role")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Role updated",
          content = @Content(schema = @Schema(implementation = RoleDto.class))),
      @ApiResponse(responseCode = "404", description = "Role not found")
  })
  public ResponseEntity<RoleDto> update(
      @Parameter(description = "Role id", required = true) @PathVariable UUID id,
      @Valid @RequestBody RoleDto dto) {
    return roleService.update(id, dto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}
