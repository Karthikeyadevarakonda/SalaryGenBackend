package com.example.salaryAquittance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who performed the action (could be Admin, HR, system scheduler, etc.)
    private String actor;

    // What action was taken: CREATE, UPDATE, DELETE, SALARY_GENERATION
    private String action;

    // Which entity was affected: STAFF, SALARY_TRANSACTION, SALARY_COMPONENT, etc.
    private String entityName;

    // ID of the affected entity
    private String entityId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String earlierState;

    // State after change (JSON string for flexibility)
    @Lob
    @Column(columnDefinition = "TEXT")
    private String laterState;

    // Timestamp of the audit log entry
    private LocalDateTime timestamp;
}

