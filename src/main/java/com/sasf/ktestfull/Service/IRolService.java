package com.sasf.ktestfull.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Dto.RolResponseDto;

@Service
public interface IRolService {
    List<RolResponseDto> listRoles();
}
