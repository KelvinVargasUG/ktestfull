package com.sasf.ktestfull.Dto;

import java.util.List;

import com.sasf.ktestfull.Dto.Validation.onCreate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class TaskRequestDto {

    @NotBlank(message = "Title cannot be blank", groups = onCreate.class)
    private String title;

    private String description;

    @NotNull(message = "Project ID cannot be null", groups = onCreate.class)
    private Long projectId;

    private List<Long> userId;

    private String status;

}
