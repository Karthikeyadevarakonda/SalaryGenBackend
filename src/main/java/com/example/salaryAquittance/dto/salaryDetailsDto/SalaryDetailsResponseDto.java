package com.example.salaryAquittance.dto.salaryDetailsDto;

import com.example.salaryAquittance.enums.StaffSalaryComponent;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SalaryDetailsResponseDto {

    private Long id;
    private BigDecimal basicPay;
    private String bankAccountNumber;
    private String ifscCode;
    private String bankName;
    private List<StaffSalaryComponent> salaryComponents = new ArrayList<>();

    // getters and setters
}
