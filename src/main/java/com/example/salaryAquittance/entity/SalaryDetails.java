package com.example.salaryAquittance.entity;

import com.example.salaryAquittance.enums.StaffSalaryComponent;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SalaryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal basicPay;

    private String bankAccountNumber;

    private String ifscCode;

    private String bankName;

    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    List<StaffSalaryComponent> salaryComponents = new ArrayList<>();


}
