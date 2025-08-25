package com.example.salaryAquittance.mapper;



import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentRequestDto;
import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentResponseDto;
import com.example.salaryAquittance.entity.SalaryComponent;
import org.springframework.stereotype.Component;

@Component
public class SalaryComponentMapper {

    public static SalaryComponent toEntity(SalaryComponentRequestDto dto) {
        if (dto == null) return null;
        SalaryComponent entity = new SalaryComponent();
        entity.setName(dto.getName());
        entity.setDepartment(dto.getDepartment());
        entity.setStaffId(dto.getStaffId());
        entity.setFixedAmount(dto.getFixedAmount());
        entity.setPercentage(dto.getPercentage());
        entity.setComponentType(dto.getComponentType());
        entity.setEffectiveDate(dto.getEffectiveDate());
        return entity;
    }

    public static SalaryComponentResponseDto toDto(SalaryComponent entity) {
        if (entity == null) return null;

        SalaryComponentResponseDto dto = new SalaryComponentResponseDto();
        dto.setId(entity.getId());
        dto.setDepartment(entity.getDepartment());
        dto.setStaffId(entity.getStaffId());
        dto.setName(entity.getName());
        dto.setFixedAmount(entity.getFixedAmount());
        dto.setPercentage(entity.getPercentage());
        dto.setComponentType(entity.getComponentType());
        dto.setEffectiveDate(entity.getEffectiveDate());

        return dto;
    }
}
