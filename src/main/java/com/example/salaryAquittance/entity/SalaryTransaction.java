package com.example.salaryAquittance.entity;

import com.example.salaryAquittance.entity.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Entity
@Table(name = "salary_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Staff staff;

    private LocalDate salaryMonth;

    private BigDecimal basicPay;
    private BigDecimal grossSalary;
    private BigDecimal totalDeductions;
    private BigDecimal netSalary;

    private LocalDate generatedDate;

    @ElementCollection
    @CollectionTable(name = "salary_component_breakdown", joinColumns = @JoinColumn(name = "salary_transaction_id"))
    @MapKeyColumn(name = "component_name")
    @Column(name = "component_amount")
    private Map<String, BigDecimal> componentBreakdown;
}
