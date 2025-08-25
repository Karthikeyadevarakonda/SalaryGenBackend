package com.example.salaryAquittance.service;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsRequestDto;
import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;

import java.util.List;

public interface SalaryDetailsService {

    SalaryDetailsResponseDto create(SalaryDetailsRequestDto dto);

    SalaryDetailsResponseDto update(Long id, SalaryDetailsRequestDto dto);

    SalaryDetailsResponseDto getById(Long id);

    List<SalaryDetailsResponseDto> getAll();

    void delete(Long id);
}
