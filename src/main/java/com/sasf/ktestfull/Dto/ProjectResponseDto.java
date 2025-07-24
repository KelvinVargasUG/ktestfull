package com.sasf.ktestfull.Dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProjectResponseDto {

    private Long idProject;

    private String name;

    private String description;

    private String status;

    private UserResponseDto owner;

    private LocalDateTime createdAt;

}
