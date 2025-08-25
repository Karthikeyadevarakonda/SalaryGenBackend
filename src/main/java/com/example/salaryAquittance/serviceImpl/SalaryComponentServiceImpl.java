package com.example.salaryAquittance.serviceImpl;

import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentRequestDto;
import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentResponseDto;
import com.example.salaryAquittance.dto.staffDto.StaffResponseDto;
import com.example.salaryAquittance.entity.SalaryComponent;
import com.example.salaryAquittance.mapper.SalaryComponentMapper;
import com.example.salaryAquittance.mapper.StaffMapper;
import com.example.salaryAquittance.repository.SalaryComponentRepository;
import com.example.salaryAquittance.service.AuditLogService;
import com.example.salaryAquittance.service.SalaryComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaryComponentServiceImpl implements SalaryComponentService {

    @Autowired
    private SalaryComponentRepository salaryComponentRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public SalaryComponentResponseDto create(SalaryComponentRequestDto dto) {
        SalaryComponent entity = SalaryComponentMapper.toEntity(dto);
        SalaryComponentResponseDto salaryComponentResponseDto = SalaryComponentMapper.toDto(salaryComponentRepository.save(entity));
        auditLogService.log("admin","CREATE","SalaryComponent",String.valueOf(salaryComponentResponseDto.getId()),null,salaryComponentResponseDto);
        return salaryComponentResponseDto;
    }

    @Override
    public SalaryComponentResponseDto update(Long id, SalaryComponentRequestDto dto) {
        Optional<SalaryComponent> optional = salaryComponentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("SalaryComponent not found with id: " + id);
        }

        SalaryComponent existing = optional.get();
        SalaryComponentResponseDto earlier = SalaryComponentMapper.toDto(existing);
        existing.setName(dto.getName());
        existing.setFixedAmount(dto.getFixedAmount());
        existing.setPercentage(dto.getPercentage());
        existing.setComponentType(dto.getComponentType());
        existing.setEffectiveDate(dto.getEffectiveDate());


        SalaryComponentResponseDto salaryComponentResponseDto = SalaryComponentMapper.toDto(salaryComponentRepository.save(existing));

        auditLogService.log("admin","UPDATE","SalaryComponent",String.valueOf(earlier.getId()),earlier,salaryComponentResponseDto);



        return salaryComponentResponseDto;
    }

    @Override
    public SalaryComponentResponseDto getById(Long id) {
        return SalaryComponentMapper.toDto(salaryComponentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalaryComponent not found with id: " + id)));
    }

    @Override
    public List<SalaryComponentResponseDto> getAll() {
        return salaryComponentRepository.findAll()
                .stream()
                .map(SalaryComponentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        SalaryComponentResponseDto earlier = SalaryComponentMapper.toDto(salaryComponentRepository.findById(id).orElseThrow(()->new RuntimeException("SalaryComponent not found with id: " + id)));

        auditLogService.log("admin","DELETE","SalaryComponent",String.valueOf(earlier.getId()),earlier,null);

        salaryComponentRepository.deleteById(id);
    }
}
