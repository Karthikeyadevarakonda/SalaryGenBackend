package com.example.salaryAquittance.serviceImpl;

import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionResponseDto;
import com.example.salaryAquittance.dto.staffDto.StaffRequestDto;
import com.example.salaryAquittance.dto.staffDto.StaffResponseDto;
import com.example.salaryAquittance.entity.*;
import com.example.salaryAquittance.enums.StaffSalaryComponent;
import com.example.salaryAquittance.mapper.StaffMapper;
import com.example.salaryAquittance.repository.SalaryComponentRepository;
import com.example.salaryAquittance.repository.SalaryTransactionRepository;
import com.example.salaryAquittance.repository.StaffRepository;
import com.example.salaryAquittance.repository.UserRepository;
import com.example.salaryAquittance.service.AuditLogService;
import com.example.salaryAquittance.service.StaffService;

import com.example.salaryAquittance.utility.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {


    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SalaryTransactionRepository salaryTransactionRepository;

    @Autowired
    private SalaryComponentRepository salaryComponentRepository;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public StaffResponseDto createStaff(StaffRequestDto dto,String role) {
        Staff staff = staffMapper.toEntity(dto);
        staff.setRelievedDate(null);
        StaffResponseDto staffResponseDto= staffMapper.toDto(staffRepository.save(staff));
        auditLogService.log(role,"CREATE","Staff",String.valueOf(staffResponseDto.getId()),null,staffResponseDto);

        /*try {
            emailService.sendWelcomeEmail(dto.getEmail(), staff.getName());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }*/


        return staffResponseDto;
    }


    @Override
    public StaffResponseDto updateStaff(Long id, StaffRequestDto dto,String role) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isEmpty()) {
            throw new RuntimeException("Staff not found with id: " + id);
        }

        Staff existing = optionalStaff.get();
        StaffResponseDto earlier = staffMapper.toDto(existing);
        existing.setName(dto.getName());
        existing.setJoiningDate(dto.getJoiningDate());
        existing.setRelievedDate(dto.getRelievedDate());
        existing.setDepartment(dto.getDepartment());

        if (dto.getSalaryDetails() != null) {
            SalaryDetails sd = getSalaryDetails(dto, existing);
            existing.setSalaryDetails(sd);
        }

        StaffResponseDto staffResponseDto = staffMapper.toDto(staffRepository.save(existing));

        auditLogService.log(role,"UPDATE","Staff",String.valueOf(earlier.getId()),earlier,staffResponseDto);


        return staffResponseDto;
    }

    private static SalaryDetails getSalaryDetails(StaffRequestDto dto, Staff existing) {
        SalaryDetails sd = existing.getSalaryDetails();

        if (sd == null) {
            sd = new SalaryDetails();
            sd.setStaff(existing);
        }

        sd.setBasicPay(dto.getSalaryDetails().getBasicPay());
        sd.setBankAccountNumber(dto.getSalaryDetails().getBankAccountNumber());
        sd.setIfscCode(dto.getSalaryDetails().getIfscCode());
        sd.setBankName(dto.getSalaryDetails().getBankName());
        sd.setSalaryComponents(dto.getSalaryDetails().getSalaryComponents());
        return sd;
    }

    @Override
    public StaffResponseDto getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
        return staffMapper.toDto(staff);
    }

    @Override
    public List<StaffResponseDto> getAllStaff() {
        return staffRepository.findByRelievedDateIsNull()
                .stream()
                .map(s->staffMapper.toDto(s))
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffResponseDto> getAllRelievedStaff() {
        return staffRepository.findByRelievedDateIsNotNull().stream().map(s->staffMapper.toDto(s)).collect(Collectors.toList());

    }


    @Override
    public void deleteStaffById(Long id,String role) {

        Staff staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        AppUser appUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found with id: " + id));


        StaffResponseDto earlier = staffMapper.toDto(staff);

        auditLogService.log(role,"DELETE","Staff",String.valueOf(earlier.getId()),earlier,null);

        staff.setRelievedDate(LocalDate.now());

        appUser.setEnabled(false);

        staffRepository.save(staff);


    }


    }









