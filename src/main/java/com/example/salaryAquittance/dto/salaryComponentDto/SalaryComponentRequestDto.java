package com.example.salaryAquittance.dto.salaryComponentDto;

import com.example.salaryAquittance.enums.ComponentType;
import com.example.salaryAquittance.enums.StaffSalaryComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryComponentRequestDto {

    private StaffSalaryComponent name;
    private String department;
    private Long staffId;
    private BigDecimal fixedAmount;
    private BigDecimal percentage;
    private ComponentType componentType;
    private LocalDate effectiveDate;
}
