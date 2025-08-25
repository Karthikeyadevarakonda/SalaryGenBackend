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
public class SalaryTransactionRequestDto {

    private Long staffId;
    private LocalDate salaryMonth;
    private BigDecimal basicPay;
    private BigDecimal grossSalary;
    private BigDecimal totalDeductions;
    private BigDecimal netSalary;
    private LocalDate generatedDate;
    private Map<String, BigDecimal> componentBreakdown;
}
