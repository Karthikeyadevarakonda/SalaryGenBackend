package com.example.salaryAquittance.service;


import com.example.salaryAquittance.dto.auditLogDto.AuditLogResponseDto;

import java.util.List;

public interface AuditLogService {
    void log(String actor, String action, String entityName, String entityId, Object earlier,Object later);
    List<AuditLogResponseDto> getAllLogs();
}
