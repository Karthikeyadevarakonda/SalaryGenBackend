package com.example.salaryAquittance.mapper;


import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;
import com.example.salaryAquittance.dto.staffDto.StaffRequestDto;
import com.example.salaryAquittance.dto.staffDto.StaffResponseDto;
import com.example.salaryAquittance.entity.*;
import com.example.salaryAquittance.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffMapper {
    private final PasswordEncoder passwordEncoder;

    public  Staff toEntity(StaffRequestDto dto) {
        if (dto == null) return null;

        AppUser user = AppUser.builder()
                .username(dto.getUserName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .isEnabled(true)
                .build();

        Staff staff = new Staff();
        staff.setName(dto.getName());
        staff.setAppUser(user);
        staff.setJoiningDate(dto.getJoiningDate());
        staff.setRelievedDate(dto.getRelievedDate());
        staff.setDepartment(dto.getDepartment());


        if (dto.getSalaryDetails() != null) {
            SalaryDetails salaryDetails = new SalaryDetails();
            salaryDetails.setBasicPay(dto.getSalaryDetails().getBasicPay());
            salaryDetails.setBankAccountNumber(dto.getSalaryDetails().getBankAccountNumber());
            salaryDetails.setIfscCode(dto.getSalaryDetails().getIfscCode());
            salaryDetails.setBankName(dto.getSalaryDetails().getBankName());
            salaryDetails.setSalaryComponents(dto.getSalaryDetails().getSalaryComponents());
            salaryDetails.setStaff(staff);
            staff.setSalaryDetails(salaryDetails);
        }

        return staff;
    }

    public  StaffResponseDto toDto(Staff staff) {
        if (staff == null) return null;

        StaffResponseDto dto = new StaffResponseDto();
        dto.setId(staff.getId());
        dto.setName(staff.getName());
        dto.setJoiningDate(staff.getJoiningDate());
        dto.setRelievedDate(staff.getRelievedDate());
        dto.setDepartment(staff.getDepartment());

        if (staff.getSalaryDetails() != null) {
            SalaryDetailsResponseDto salaryDetailsDto = new SalaryDetailsResponseDto();
            salaryDetailsDto.setId(staff.getSalaryDetails().getId());
            salaryDetailsDto.setBasicPay(staff.getSalaryDetails().getBasicPay());
            salaryDetailsDto.setBankAccountNumber(staff.getSalaryDetails().getBankAccountNumber());
            salaryDetailsDto.setIfscCode(staff.getSalaryDetails().getIfscCode());
            salaryDetailsDto.setBankName(staff.getSalaryDetails().getBankName());
            salaryDetailsDto.setSalaryComponents(staff.getSalaryDetails().getSalaryComponents());
            dto.setSalaryDetails(salaryDetailsDto);
        }

        return dto;
    }
}
