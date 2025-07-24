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
public class UserResponseDto {

    private Long idUser;

    private String username;

    private String email;

    private String status;

    private List<String> rol;
}
