package com.example.salaryAquittance.mapper;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsRequestDto;
import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;
import com.example.salaryAquittance.entity.SalaryDetails;
import org.springframework.stereotype.Component;

@Component
public class SalaryDetailsMapper {

    public static SalaryDetails toEntity(SalaryDetailsRequestDto dto) {
        if (dto == null) return null;

        SalaryDetails entity = new SalaryDetails();
        entity.setBasicPay(dto.getBasicPay());
        entity.setBankAccountNumber(dto.getBankAccountNumber());
        entity.setIfscCode(dto.getIfscCode());
        entity.setBankName(dto.getBankName());

        entity.setSalaryComponents(dto.getSalaryComponents());

        return entity;
    }

    public static SalaryDetailsResponseDto toDto(SalaryDetails entity) {
        if (entity == null) return null;

        SalaryDetailsResponseDto dto = new SalaryDetailsResponseDto();
        dto.setId(entity.getId());
        dto.setBasicPay(entity.getBasicPay());
        dto.setBankAccountNumber(entity.getBankAccountNumber());
        dto.setIfscCode(entity.getIfscCode());
        dto.setBankName(entity.getBankName());
        dto.setSalaryComponents(entity.getSalaryComponents());

        return dto;
    }
}
