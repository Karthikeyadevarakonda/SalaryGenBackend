package com.example.salaryAquittance.dto.salaryTransactionDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryTransactionResponseDto {

    private Long id;
    private Long staffId;
    private String staffName;
    private boolean isActive;
    private YearMonth salaryMonth;
    private BigDecimal basicPay;
    private BigDecimal grossSalary;
    private BigDecimal totalDeductions;
    private BigDecimal netSalary;
    private LocalDate generatedDate;
    private Map<String, BigDecimal> componentBreakdown;
}
