package com.example.salaryAquittance.controller;


import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionRequestDto;
import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionResponseDto;
import com.example.salaryAquittance.service.SalaryTransactionService;
import com.example.salaryAquittance.utility.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.YearMonth;
import java.util.List;

@RestController 
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class SalaryTransactionController {

    private final SalaryTransactionService salaryTransactionService;
    private final PayslipService payslipService;

    @PostMapping("/admin/salary-transactions")
    public ResponseEntity<SalaryTransactionResponseDto> createSalaryTransaction(@RequestBody SalaryTransactionRequestDto dto) {
        SalaryTransactionResponseDto created = salaryTransactionService.create(dto);
        return ResponseEntity.created(URI.create("/api/salary-transactions/" + created.getId())).body(created);
    }

    @PutMapping("/admin/salary-transactions/{id}")
    public ResponseEntity<SalaryTransactionResponseDto> updateSalaryTransaction(@PathVariable Long id, @RequestBody SalaryTransactionRequestDto dto) {
        SalaryTransactionResponseDto updated = salaryTransactionService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/admin/salary-transactions/{id}")
    public ResponseEntity<SalaryTransactionResponseDto> getSalaryTransactionById(@PathVariable Long id) {
        SalaryTransactionResponseDto transaction = salaryTransactionService.getById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/hr/salary-transactions")
    public ResponseEntity<List<SalaryTransactionResponseDto>> getAllSalaryTransactions() {
        return ResponseEntity.ok(salaryTransactionService.getAll());
    }

    @DeleteMapping("/admin/salary-transactions/{id}")
    public ResponseEntity<Void> deleteSalaryTransaction(@PathVariable Long id) {
        salaryTransactionService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/hr/salary-transactions/generate")
    public ResponseEntity<List<SalaryTransactionResponseDto>> generateSalaryTransactions(
            @RequestParam int year,
            @RequestParam int month) {
        YearMonth salaryMonth = YearMonth.of(year, month);
        List<SalaryTransactionResponseDto> transactions = salaryTransactionService.generateSalaryForAllStaff(salaryMonth);
        return ResponseEntity.ok(transactions);
    }


    // ADMIN

    @GetMapping("/hr/salary-transactions/month")
    public ResponseEntity<List<SalaryTransactionResponseDto>> getByMonth(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(salaryTransactionService.getAllByMonth(month));
    }

    @GetMapping("/hr/salary-transactions/range")
    public ResponseEntity<List<SalaryTransactionResponseDto>> getByRange(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM") YearMonth start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM") YearMonth end) {
        return ResponseEntity.ok(salaryTransactionService.getAllBetweenMonths(start, end));
    }

    @GetMapping("/hr/salary-transactions/staff/{staffId}/all")
    public ResponseEntity<List<SalaryTransactionResponseDto>> getByStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(salaryTransactionService.getAllByStaff(staffId));
    }

    @GetMapping("/hr/salary-transactions/staff/{staffId}/month")
    public ResponseEntity<SalaryTransactionResponseDto> getByStaffAndMonth(
            @PathVariable Long staffId,
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(salaryTransactionService.getByStaffAndMonth(staffId, month));
    }

    @GetMapping("/hr/salary-transactions/staff/{staffId}/latest")
    public ResponseEntity<SalaryTransactionResponseDto> getStaffLatestByHr(@PathVariable Long staffId) {
        return ResponseEntity.ok(salaryTransactionService.getLatestSalaryForStaff(staffId));
    }

    @GetMapping("/hr/salary-transactions/latest")
    public ResponseEntity<List<SalaryTransactionResponseDto>> getLatestSalaries() {
        return ResponseEntity.ok(salaryTransactionService.getLatestMonthSalaries());
    }

    // STAFF

    @GetMapping("/staff/salary-transactions/{staffId}/all")
    public ResponseEntity<List<SalaryTransactionResponseDto>> getStaffAll(@PathVariable Long staffId) {
        return ResponseEntity.ok(salaryTransactionService.getAllByStaff(staffId));
    }


    @GetMapping("/staff/salary-transactions/{staffId}/month")
    public ResponseEntity<SalaryTransactionResponseDto> getStaffAndMonth(
            @PathVariable Long staffId,
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(salaryTransactionService.getByStaffAndMonth(staffId, month));
    }

    @GetMapping("/staff/salary-transactions/{staffId}/latest")
    public ResponseEntity<SalaryTransactionResponseDto> getStaffLatest(@PathVariable Long staffId) {
        return ResponseEntity.ok(salaryTransactionService.getLatestSalaryForStaff(staffId));
    }


    @GetMapping("/generate-payslip/{staffId}")
    public ResponseEntity<Resource> downloadPayslip(
            @PathVariable Long staffId,
            @RequestParam YearMonth salaryMonth) throws IOException {

        File file = payslipService.generatePayslip(staffId, salaryMonth);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length())
                .body(resource);
    }




}
