package com.example.salaryAquittance.dto.staffDto;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;
import lombok.Data;

import java.time.LocalDate;


@Data
public class StaffResponseDto {

    private Long id;
    private String name;
    private LocalDate joiningDate;
    private LocalDate relievedDate;
    private String department;
    private SalaryDetailsResponseDto salaryDetails;

    // getters and setters
}
