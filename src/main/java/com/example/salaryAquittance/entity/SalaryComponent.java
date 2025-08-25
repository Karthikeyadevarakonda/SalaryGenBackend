package com.example.salaryAquittance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.salaryAquittance.enums.StaffSalaryComponent;
import com.example.salaryAquittance.enums.ComponentType;
import lombok.Data;

@Entity
@Data
public class SalaryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StaffSalaryComponent name;

    private BigDecimal fixedAmount;

    private String department;

    private Long staffId;

    private BigDecimal percentage;

    @Enumerated(EnumType.STRING)
    private ComponentType componentType;

    private LocalDate effectiveDate;

    // getters and setters
}
