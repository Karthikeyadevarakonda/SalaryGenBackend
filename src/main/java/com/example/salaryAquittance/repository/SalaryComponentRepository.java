package com.example.salaryAquittance.repository;

import com.example.salaryAquittance.entity.SalaryComponent;
import com.example.salaryAquittance.enums.ComponentType;
import com.example.salaryAquittance.enums.StaffSalaryComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryComponentRepository extends JpaRepository<SalaryComponent, Long> {

    List<SalaryComponent> findByComponentType(ComponentType componentType); // ALLOWANCE / DEDUCTION
    List<SalaryComponent> findByNameAndStaffIdIsNullAndDepartmentIsNullAndEffectiveDateLessThanEqual(StaffSalaryComponent name, LocalDate date);

    // Most recent applicable component (latest before the given date)
    @Query("SELECT sc FROM SalaryComponent sc WHERE sc.name = :name AND sc.effectiveDate <= :date ORDER BY sc.effectiveDate DESC")
    List<SalaryComponent> findLatestComponentByNameBeforeDate(StaffSalaryComponent name, LocalDate date);

    List<SalaryComponent> findByNameAndDepartmentAndStaffIdIsNullAndEffectiveDateLessThanEqual(StaffSalaryComponent name,String department, LocalDate date);

    List<SalaryComponent> findByNameAndStaffIdAndDepartmentIsNullAndEffectiveDateLessThanEqual(StaffSalaryComponent name,Long staffId, LocalDate date);

    List<SalaryComponent> findByStaffId(Long staffId);

}
