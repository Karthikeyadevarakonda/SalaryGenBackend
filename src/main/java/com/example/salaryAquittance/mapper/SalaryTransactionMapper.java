package com.example.salaryAquittance.mapper;

import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionRequestDto;
import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionResponseDto;
import com.example.salaryAquittance.entity.SalaryTransaction;
import com.example.salaryAquittance.entity.Staff;
import com.example.salaryAquittance.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class SalaryTransactionMapper {

    public SalaryTransaction toEntity(SalaryTransactionRequestDto dto) {
        if (dto == null) {
            return null;
        }

        SalaryTransaction entity = new SalaryTransaction();
        entity.setSalaryMonth(dto.getSalaryMonth());
        entity.setBasicPay(dto.getBasicPay());
        entity.setGrossSalary(dto.getGrossSalary());
        entity.setTotalDeductions(dto.getTotalDeductions());
        entity.setNetSalary(dto.getNetSalary());
        entity.setGeneratedDate(dto.getGeneratedDate());
        entity.setComponentBreakdown(dto.getComponentBreakdown());

        return entity;
    }

    public SalaryTransactionResponseDto toDto(SalaryTransaction entity) {
        if (entity == null) {
            return null;
        }

        SalaryTransactionResponseDto dto = new SalaryTransactionResponseDto();
        dto.setId(entity.getId());
        dto.setStaffId(entity.getStaff() != null ? entity.getStaff().getId() : null);
        dto.setStaffName(entity.getStaff()!=null ? entity.getStaff().getName():null);
        dto.setActive(entity.getStaff() != null && entity.getStaff().getRelievedDate() == null);
        dto.setSalaryMonth(YearMonth.from(entity.getSalaryMonth()));
        dto.setBasicPay(entity.getBasicPay());
        dto.setGrossSalary(entity.getGrossSalary());
        dto.setTotalDeductions(entity.getTotalDeductions());
        dto.setNetSalary(entity.getNetSalary());
        dto.setGeneratedDate(entity.getGeneratedDate());
        dto.setComponentBreakdown(entity.getComponentBreakdown());

        return dto;
    }

}
