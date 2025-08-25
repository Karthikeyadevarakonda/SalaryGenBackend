package com.example.salaryAquittance.dto.auditLogDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogResponseDto {
    private Long id;
    private String actor;
    private String action;
    private String entityName;
    private String entityId;
    private String earlierState;
    private String laterState;
    private LocalDateTime timestamp;
}
