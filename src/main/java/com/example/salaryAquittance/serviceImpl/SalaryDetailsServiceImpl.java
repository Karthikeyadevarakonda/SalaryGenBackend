package com.example.salaryAquittance.serviceImpl;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsRequestDto;
import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;
import com.example.salaryAquittance.entity.SalaryDetails;
import com.example.salaryAquittance.mapper.SalaryDetailsMapper;
import com.example.salaryAquittance.repository.SalaryDetailsRepository;
import com.example.salaryAquittance.service.AuditLogService;
import com.example.salaryAquittance.service.SalaryDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaryDetailsServiceImpl implements SalaryDetailsService {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private SalaryDetailsRepository salaryDetailsRepository;

    @Override
    public SalaryDetailsResponseDto create(SalaryDetailsRequestDto dto) {
        SalaryDetails entity = SalaryDetailsMapper.toEntity(dto);
        return SalaryDetailsMapper.toDto(salaryDetailsRepository.save(entity));
    }

    @Override
    public SalaryDetailsResponseDto update(Long id, SalaryDetailsRequestDto dto) {
        Optional<SalaryDetails> optional = salaryDetailsRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("SalaryDetails not found with id: " + id);
        }

        SalaryDetails existing = optional.get();
        existing.setBasicPay(dto.getBasicPay());
        existing.setBankAccountNumber(dto.getBankAccountNumber());
        existing.setIfscCode(dto.getIfscCode());
        existing.setBankName(dto.getBankName());
        existing.setSalaryComponents(dto.getSalaryComponents());

        return SalaryDetailsMapper.toDto(salaryDetailsRepository.save(existing));
    }

    @Override
    public SalaryDetailsResponseDto getById(Long id) {
        return salaryDetailsRepository.findById(id)
                .map(SalaryDetailsMapper::toDto)
                .orElseThrow(() -> new RuntimeException("SalaryDetails not found with id: " + id));
    }

    @Override
    public List<SalaryDetailsResponseDto> getAll() {
        return salaryDetailsRepository.findAll()
                .stream()
                .map(SalaryDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!salaryDetailsRepository.existsById(id)) {
            throw new RuntimeException("SalaryDetails not found with id: " + id);
        }
        salaryDetailsRepository.deleteById(id);
    }
}
