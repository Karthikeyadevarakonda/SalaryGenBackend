package com.example.salaryAquittance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @MapsId
    private AppUser appUser;

    private LocalDate joiningDate;

    private LocalDate relievedDate;

    private String department;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private SalaryDetails salaryDetails;

    // getters and setters
}
