package de.csc.hpforum.user.controller;

import de.csc.hpforum.user.model.dto.UserDto;
import de.csc.hpforum.user.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "List users", description = "Returns all users")
  public List<UserDto> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get user", description = "Returns the user for the given id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User found",
          content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<UserDto> findById(
      @Parameter(description = "User id", required = true) @PathVariable UUID id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create user")
  @ApiResponse(responseCode = "201", description = "User created",
      content = @Content(schema = @Schema(implementation = UserDto.class)))
  public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
    UserDto created = userService.create(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update user")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User updated",
          content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<UserDto> update(
      @Parameter(description = "User id", required = true) @PathVariable UUID id,
      @Valid @RequestBody UserDto dto) {
    return userService.update(id, dto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}
