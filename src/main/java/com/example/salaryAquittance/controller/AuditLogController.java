package com.example.salaryAquittance.controller;


import com.example.salaryAquittance.dto.auditLogDto.AuditLogResponseDto;
import com.example.salaryAquittance.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public List<AuditLogResponseDto> getAllAuditLogs() {
        return auditLogService.getAllLogs();
    }
}
