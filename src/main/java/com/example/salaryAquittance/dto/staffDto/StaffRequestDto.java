package com.example.salaryAquittance.dto.staffDto;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsRequestDto;
import com.example.salaryAquittance.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffRequestDto {

    @NotNull(message = "username is required")
    private String userName;
    @NotNull(message = "password is required")
    private String password;
    @NotNull(message ="role is required")
    private Role role;

    private String name;
    @NotNull
    private String email;
    private LocalDate joiningDate;
    private LocalDate relievedDate;
    private String department;
    private SalaryDetailsRequestDto salaryDetails;
}
