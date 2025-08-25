package com.example.salaryAquittance.service;


import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionRequestDto;
import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionResponseDto;

import java.time.YearMonth;
import java.util.List;

public interface SalaryTransactionService {

    SalaryTransactionResponseDto create(SalaryTransactionRequestDto dto);

    SalaryTransactionResponseDto update(Long id, SalaryTransactionRequestDto dto);

    SalaryTransactionResponseDto getById(Long id);

    List<SalaryTransactionResponseDto> getAll();

    void delete(Long id);
    List<SalaryTransactionResponseDto> generateSalaryForAllStaff(YearMonth month);

    List<SalaryTransactionResponseDto> getAllByMonth(YearMonth month);
    List<SalaryTransactionResponseDto> getAllBetweenMonths(YearMonth start, YearMonth end);
    List<SalaryTransactionResponseDto> getAllByStaff(Long staffId);
    SalaryTransactionResponseDto getByStaffAndMonth(Long staffId, YearMonth month);
    List<SalaryTransactionResponseDto> getLatestMonthSalaries();
    SalaryTransactionResponseDto getLatestSalaryForStaff(Long staffId);


}
