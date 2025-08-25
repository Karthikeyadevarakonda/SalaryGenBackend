package com.example.salaryAquittance.serviceImpl;


import com.example.salaryAquittance.dto.auditLogDto.AuditLogResponseDto;
import com.example.salaryAquittance.entity.AuditLog;
import com.example.salaryAquittance.repository.AuditLogRepository;
import com.example.salaryAquittance.service.AuditLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public void log(String actor, String action, String entityName, String entityId, Object earlier, Object later) {
        try {
            AuditLog log = AuditLog.builder()
                    .actor(actor)
                    .action(action)
                    .entityName(entityName)
                    .entityId(entityId)
                    .earlierState(objectMapper.writeValueAsString(earlier))
                    .laterState(objectMapper.writeValueAsString(later))
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(log);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing audit log objects", e);
        }
    }


    @Override
    public List<AuditLogResponseDto> getAllLogs() {
        return auditLogRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AuditLogResponseDto convertToDto(AuditLog auditLog) {
        return AuditLogResponseDto.builder()
                .id(auditLog.getId())
                .actor(auditLog.getActor())
                .action(auditLog.getAction())
                .entityName(auditLog.getEntityName())
                .entityId(auditLog.getEntityId())
                .earlierState(auditLog.getEarlierState())
                .laterState(auditLog.getLaterState())
                .timestamp(auditLog.getTimestamp())
                .build();
    }

}
