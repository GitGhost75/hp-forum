package de.csc.hpforum.organization.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDto {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 64)
    private String shortName;

    @NotBlank
    @Size(max = 64)
    private String acnClientNumber;

    @NotNull
    private UUID organizationCategoryId;

}
