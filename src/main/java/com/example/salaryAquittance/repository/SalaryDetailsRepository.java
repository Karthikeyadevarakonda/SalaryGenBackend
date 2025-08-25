package com.example.salaryAquittance.repository;

import com.example.salaryAquittance.entity.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryDetailsRepository extends JpaRepository<SalaryDetails, Long> {

    List<SalaryDetails> findByStaffId(Long staffId); // Get salary details of a staff

}
