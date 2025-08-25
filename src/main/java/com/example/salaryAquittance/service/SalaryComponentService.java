package com.example.salaryAquittance.service;




import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentRequestDto;
import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentResponseDto;

import java.util.List;


public interface SalaryComponentService {

    SalaryComponentResponseDto create(SalaryComponentRequestDto dto);

    SalaryComponentResponseDto update(Long id, SalaryComponentRequestDto dto);

    SalaryComponentResponseDto getById(Long id);

    List<SalaryComponentResponseDto> getAll();

    void delete(Long id);
}
