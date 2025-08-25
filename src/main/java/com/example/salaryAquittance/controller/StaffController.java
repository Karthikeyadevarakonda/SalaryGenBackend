package com.example.salaryAquittance.controller;

import com.example.salaryAquittance.auth.SecurityUtil;
import com.example.salaryAquittance.auth.UserPrincipal;
import com.example.salaryAquittance.dto.staffDto.StaffRequestDto;
import com.example.salaryAquittance.dto.staffDto.StaffResponseDto;
import com.example.salaryAquittance.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StaffController {

    private final StaffService staffService;

    // CREATE
    @PostMapping("/admin/staff")
    public ResponseEntity<StaffResponseDto> createStaff(@Valid @RequestBody StaffRequestDto dto) {
        UserPrincipal userPrincipal = SecurityUtil.getCurrentUser();
        StaffResponseDto createdStaff = staffService.createStaff(dto,userPrincipal.getAppUser().getRole().name());
        return ResponseEntity.created(URI.create("/api/staff/" + createdStaff.getId()))
                .body(createdStaff);
    }

    @PutMapping("/admin/staff/{id}")
    public ResponseEntity<StaffResponseDto> updateStaffByAdmin(@PathVariable Long id, @RequestBody StaffRequestDto dto) {
        UserPrincipal userPrincipal = SecurityUtil.getCurrentUser();
        StaffResponseDto updatedStaff = staffService.updateStaff(id, dto,userPrincipal.getAppUser().getRole().name());
        return ResponseEntity.ok(updatedStaff);
    }


    // UPDATE
    @PutMapping("/staff/{id}")
    public ResponseEntity<StaffResponseDto> updateStaff(@PathVariable Long id, @RequestBody StaffRequestDto dto) {
        UserPrincipal userPrincipal = SecurityUtil.getCurrentUser();
        StaffResponseDto updatedStaff = staffService.updateStaff(id, dto,userPrincipal.getAppUser().getRole().name());
        return ResponseEntity.ok(updatedStaff);
    }

    @GetMapping("/admin/staff/{id}")
    public ResponseEntity<StaffResponseDto> getStaffByIdByAdmin(@PathVariable Long id) {
        StaffResponseDto staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/hr/staff/{id}")
    public ResponseEntity<StaffResponseDto> getStaffByIdByHr(@PathVariable Long id) {
        StaffResponseDto staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    // GET BY ID
    @GetMapping("/staff/{id}")
    public ResponseEntity<StaffResponseDto> getStaffById(@PathVariable Long id) {
        StaffResponseDto staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }


    // GET ALL
    @GetMapping("/admin/staff")
    public ResponseEntity<List<StaffResponseDto>> getAllStaff() {
        List<StaffResponseDto> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    // GET ALL
    @GetMapping("/hr/staff")
    public ResponseEntity<List<StaffResponseDto>> getAllStaffByHr() {
        List<StaffResponseDto> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    // DELETE
    @DeleteMapping("/admin/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        UserPrincipal userPrincipal = SecurityUtil.getCurrentUser();
        staffService.deleteStaffById(id,userPrincipal.getAppUser().getRole().name());
        return ResponseEntity.noContent().build();
    }

    // RELIEVED
    @GetMapping("/admin/staff/relieved")
    public ResponseEntity<List<StaffResponseDto>> getAllRelievedStaff() {
        List<StaffResponseDto> staffList = staffService.getAllRelievedStaff();
        return ResponseEntity.ok(staffList);
    }

}
