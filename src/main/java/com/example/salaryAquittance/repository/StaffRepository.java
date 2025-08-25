package com.example.salaryAquittance.repository;

import com.example.salaryAquittance.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff>findByRelievedDateIsNull();
    List<Staff>findByRelievedDateIsNotNull();

    @Query("SELECT s.name from Staff s where s.id=:staffId")
    String findNameByStaffId(@Param("staffId") Long id);
}
