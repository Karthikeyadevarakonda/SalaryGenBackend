package com.example.salaryAquittance.controller;



import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentRequestDto;
import com.example.salaryAquittance.dto.salaryComponentDto.SalaryComponentResponseDto;
import com.example.salaryAquittance.entity.SalaryComponent;
import com.example.salaryAquittance.mapper.SalaryComponentMapper;
import com.example.salaryAquittance.repository.SalaryComponentRepository;
import com.example.salaryAquittance.service.SalaryComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hr/salary-components")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class SalaryComponentController {

    private final SalaryComponentService salaryComponentService;
    private final SalaryComponentRepository salaryComponentRepository;
    private final SalaryComponentMapper salaryComponentMapper;

    @PostMapping
    public ResponseEntity<SalaryComponentResponseDto> createSalaryComponent(@RequestBody SalaryComponentRequestDto dto) {
        SalaryComponentResponseDto created = salaryComponentService.create(dto);
        return ResponseEntity.created(URI.create("/api/salary-components/" + created.getId())).body(created);
    }

    @PostMapping("/staff/{staffId}")
    public ResponseEntity<SalaryComponentResponseDto> createSalaryComponent(@RequestBody SalaryComponentRequestDto dto,@PathVariable Long staffId) {
        dto.setStaffId(staffId);
        SalaryComponentResponseDto created = salaryComponentService.create(dto);
        return ResponseEntity.created(URI.create("/api/salary-components/" + created.getId())).body(created);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<SalaryComponentResponseDto>> getSalaryComponentByStaffId(@PathVariable Long staffId) {
        List<SalaryComponent> components = salaryComponentRepository.findByStaffId(staffId);
        return ResponseEntity.ok(components.stream().map(SalaryComponentMapper::toDto).collect(Collectors.toList()));
    }

    @PutMapping("/staff/{staffId}/{id}")
    public ResponseEntity<SalaryComponentResponseDto> updateSalaryComponentByStaffId(@RequestBody SalaryComponentRequestDto dto,@PathVariable Long staffId,@PathVariable Long id) {
        dto.setStaffId(staffId);
        SalaryComponentResponseDto updated = salaryComponentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/staff/{staffId}/{id}")
    public ResponseEntity<Void> deleteSalaryComponentByStaffId(@PathVariable Long staffId,@PathVariable Long id) {
        salaryComponentService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/{id}")
    public ResponseEntity<SalaryComponentResponseDto> updateSalaryComponent(@PathVariable Long id, @RequestBody SalaryComponentRequestDto dto) {
        SalaryComponentResponseDto updated = salaryComponentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SalaryComponentResponseDto> getSalaryComponentById(@PathVariable Long id) {
        SalaryComponentResponseDto component = salaryComponentService.getById(id);
        return ResponseEntity.ok(component);
    }


    @GetMapping
    public ResponseEntity<List<SalaryComponentResponseDto>> getAllSalaryComponents() {
        return ResponseEntity.ok(salaryComponentService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryComponent(@PathVariable Long id) {
        salaryComponentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
