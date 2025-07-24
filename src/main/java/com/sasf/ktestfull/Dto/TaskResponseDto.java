package com.sasf.ktestfull.Dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class TaskResponseDto {

    private Long idTask;

    private String title;

    private String description;

    private List<UserResponseDto> assignedUsersTask;

    private String status;
}
