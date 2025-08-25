package com.example.salaryAquittance.service;

import com.example.salaryAquittance.dto.staffDto.StaffRequestDto;
import com.example.salaryAquittance.dto.staffDto.StaffResponseDto;

import java.util.List;

public interface StaffService {

    StaffResponseDto createStaff(StaffRequestDto dto,String role);

    StaffResponseDto updateStaff(Long id, StaffRequestDto dto,String role);

    StaffResponseDto getStaffById(Long id);

    List<StaffResponseDto> getAllStaff();

    List<StaffResponseDto> getAllRelievedStaff();

    void deleteStaffById(Long id,String role);
}
