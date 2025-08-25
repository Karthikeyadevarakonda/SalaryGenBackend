package com.example.salaryAquittance.controller;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsRequestDto;
import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;

import com.example.salaryAquittance.service.SalaryDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/salary-details")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SalaryDetailsController {

    private final SalaryDetailsService salaryDetailsService;

    // CREATE
    @PostMapping
    public ResponseEntity<SalaryDetailsResponseDto> createSalaryDetails(@RequestBody SalaryDetailsRequestDto dto) {
        SalaryDetailsResponseDto created = salaryDetailsService.create(dto);
        return ResponseEntity.created(URI.create("/api/salary-details/" + created.getId())).body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SalaryDetailsResponseDto> updateSalaryDetails(
            @PathVariable Long id,
            @RequestBody SalaryDetailsRequestDto dto) {
        SalaryDetailsResponseDto updated = salaryDetailsService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<SalaryDetailsResponseDto> getSalaryDetailsById(@PathVariable Long id) {
        SalaryDetailsResponseDto details = salaryDetailsService.getById(id);
        return ResponseEntity.ok(details);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<SalaryDetailsResponseDto>> getAllSalaryDetails() {
        List<SalaryDetailsResponseDto> list = salaryDetailsService.getAll();
        return ResponseEntity.ok(list);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryDetails(@PathVariable Long id) {
        salaryDetailsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
