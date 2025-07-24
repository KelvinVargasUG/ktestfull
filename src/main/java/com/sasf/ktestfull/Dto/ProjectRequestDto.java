package com.sasf.ktestfull.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sasf.ktestfull.Dto.Validation.onCreate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDto {

    @NotBlank(message = "Name cannot be blank", groups = onCreate.class)
    private String name;

    @NotBlank(message = "Description cannot be blank", groups = onCreate.class)
    private String description;

    @NotNull(message = "User ID cannot be null", groups = onCreate.class)
    private Long owner;

    @JsonIgnore
    private String status;

}
