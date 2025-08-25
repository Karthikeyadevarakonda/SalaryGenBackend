package com.example.salaryAquittance.repository;


import com.example.salaryAquittance.entity.SalaryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryTransactionRepository extends JpaRepository<SalaryTransaction, Long> {

List<SalaryTransaction> findBySalaryMonth(LocalDate month);
List<SalaryTransaction>findBySalaryMonthBetweenOrderBySalaryMonthAsc(LocalDate start,LocalDate end);
List<SalaryTransaction>findByStaffIdOrderBySalaryMonthAsc(long staffId);
Optional<SalaryTransaction> findByStaffIdAndSalaryMonth(long staffId, LocalDate month);
@Query("SELECT s FROM SalaryTransaction s WHERE s.salaryMonth=( SELECT MAX(s1.salaryMonth) FROM SalaryTransaction s1)")
List<SalaryTransaction> findAllLatest();
@Query("SELECT s from SalaryTransaction s WHERE s.staff.id=:staffId AND s.salaryMonth=(SELECT MAX(s1.salaryMonth) FROM SalaryTransaction s1 WHERE s1.staff.id=:staffId)")
Optional<SalaryTransaction> findLatestByStaffId(@Param("staffId") long staffId);
boolean existsBySalaryMonth(LocalDate month);
}
