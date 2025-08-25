package com.example.salaryAquittance.serviceImpl;

import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionRequestDto;
import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionResponseDto;
import com.example.salaryAquittance.entity.*;
import com.example.salaryAquittance.enums.StaffSalaryComponent;
import com.example.salaryAquittance.mapper.SalaryTransactionMapper;
import com.example.salaryAquittance.repository.SalaryComponentRepository;
import com.example.salaryAquittance.repository.SalaryTransactionRepository;
import com.example.salaryAquittance.repository.StaffRepository;
import com.example.salaryAquittance.service.AuditLogService;
import com.example.salaryAquittance.service.SalaryTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalaryTransactionServiceImpl implements SalaryTransactionService {

    private final SalaryTransactionRepository salaryTransactionRepository;
    private final StaffRepository staffRepository;
    private final SalaryTransactionMapper salaryTransactionMapper;
    private final SalaryComponentRepository salaryComponentRepository;
    private final AuditLogService auditLogService;


    @Override
    public SalaryTransactionResponseDto create(SalaryTransactionRequestDto dto) {
        Staff staff = staffRepository.findById(dto.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + dto.getStaffId()));

        SalaryTransaction transaction = salaryTransactionMapper.toEntity(dto);
        transaction.setStaff(staff);

        SalaryTransaction saved = salaryTransactionRepository.save(transaction);
        return salaryTransactionMapper.toDto(saved);
    }

    @Override
    public SalaryTransactionResponseDto update(Long id, SalaryTransactionRequestDto dto) {
        SalaryTransaction existing = salaryTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalaryTransaction not found with ID: " + id));

        Staff staff = staffRepository.findById(dto.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + dto.getStaffId()));

        existing.setStaff(staff);
        existing.setSalaryMonth(dto.getSalaryMonth());
        existing.setBasicPay(dto.getBasicPay());
        existing.setGrossSalary(dto.getGrossSalary());
        existing.setTotalDeductions(dto.getTotalDeductions());
        existing.setNetSalary(dto.getNetSalary());
        existing.setGeneratedDate(dto.getGeneratedDate());
        existing.setComponentBreakdown(dto.getComponentBreakdown());

        SalaryTransaction updated = salaryTransactionRepository.save(existing);
        return salaryTransactionMapper.toDto(updated);
    }

    @Override
    public SalaryTransactionResponseDto getById(Long id) {
        SalaryTransaction transaction = salaryTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalaryTransaction not found with ID: " + id));
        return salaryTransactionMapper.toDto(transaction);
    }

    @Override
    public List<SalaryTransactionResponseDto> getAll() {
        return salaryTransactionRepository.findAll().stream()
                .map(salaryTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!salaryTransactionRepository.existsById(id)) {
            throw new RuntimeException("SalaryTransaction not found with ID: " + id);
        }
        salaryTransactionRepository.deleteById(id);
    }


    public List<SalaryTransactionResponseDto> generateSalaryForAllStaff(YearMonth month) {

        LocalDate monthStart = month.atDay(1);

//        if(salaryTransactionRepository.existsBySalaryMonth(monthStart)){
//            throw new RuntimeException("salaries already generated for "+month.getMonth().getDisplayName(TextStyle.FULL,Locale.ENGLISH) +" month ");
//        }

        LocalDate monthEnd = month.atEndOfMonth();

        List<SalaryTransactionResponseDto> result = new ArrayList<>();

        List<Staff> allStaff = staffRepository.findAll();

        for (Staff staff : allStaff) {
            if ((staff.getRelievedDate() != null && staff.getRelievedDate().isBefore(monthStart)) || staff.getJoiningDate().isAfter(monthEnd)) continue;
            long totalDays = monthEnd.toEpochDay()-monthStart.toEpochDay()+1;
            LocalDate start = staff.getJoiningDate().isAfter(monthStart)
                    ? staff.getJoiningDate()
                    : monthStart;

            LocalDate end = (staff.getRelievedDate() == null || staff.getRelievedDate().isAfter(monthEnd))
                    ? monthEnd
                    : staff.getRelievedDate();
            long workingDays = end.toEpochDay()-start.toEpochDay()+1;
            //safety purpose
            if(workingDays<0)workingDays=0;
            SalaryDetails salaryDetails = staff.getSalaryDetails();
            if (salaryDetails == null) continue;

            BigDecimal basicPay = salaryDetails.getBasicPay();
            basicPay = basicPay.divide(BigDecimal.valueOf(totalDays),4,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(workingDays));
            Map<String, BigDecimal> componentBreakdown = new HashMap<>();

            BigDecimal totalAllowances = BigDecimal.ZERO;
            BigDecimal totalDeductions = BigDecimal.ZERO;

            List<StaffSalaryComponent> salaryComponents = salaryDetails.getSalaryComponents();


                for (StaffSalaryComponent compType : salaryComponents) {
                    List<SalaryComponent> componentChanges = salaryComponentRepository
                            .findByNameAndStaffIdAndDepartmentIsNullAndEffectiveDateLessThanEqual(compType,staff.getId(), monthEnd);
                    log.info("id: {}",staff.getId());
                    log.info("custom");
                    if(componentChanges.isEmpty()){
                        componentChanges = salaryComponentRepository.findByNameAndDepartmentAndStaffIdIsNullAndEffectiveDateLessThanEqual(compType,staff.getDepartment(),monthEnd);
                    log.info("department");
                    }

                    if(componentChanges.isEmpty()){
                        componentChanges = salaryComponentRepository.findByNameAndStaffIdIsNullAndDepartmentIsNullAndEffectiveDateLessThanEqual(compType,monthEnd);
                    log.info("global");
                    }

                    componentChanges.sort(Comparator.comparing(SalaryComponent::getEffectiveDate));
                    BigDecimal componentAmount = calculateProratedComponent(compType, componentChanges, basicPay, monthStart, monthEnd, staff.getRelievedDate());

                    if (componentAmount != null) {
                        componentBreakdown.put(compType.name(), componentAmount);

                        if (isDeduction(compType)) {
                            totalDeductions = totalDeductions.add(componentAmount);
                        } else {
                            totalAllowances = totalAllowances.add(componentAmount);
                        }
                    }
                }

            BigDecimal gross = basicPay.add(totalAllowances);
            BigDecimal net = gross.subtract(totalDeductions);

            SalaryTransaction transaction = SalaryTransaction.builder()
                    .staff(staff)
                    .salaryMonth(month.atDay(1))
                    .basicPay(basicPay)
                    .grossSalary(gross)
                    .totalDeductions(totalDeductions)
                    .netSalary(net)
                    .generatedDate(LocalDate.now())
                    .componentBreakdown(componentBreakdown)
                    .build();

            salaryTransactionRepository.save(transaction);

            SalaryTransactionResponseDto salaryTransactionResponseDto = new SalaryTransactionResponseDto(
                    transaction.getId(),
                    staff.getId(),
                    staff.getName(),
                    staff.getRelievedDate()==null,
                    month,
                    basicPay,
                    gross,
                    totalDeductions,
                    net,
                    transaction.getGeneratedDate(),
                    componentBreakdown
            );

            auditLogService.log("admin","CREATE","SalaryTransaction",String.valueOf(salaryTransactionResponseDto.getId()),null,salaryTransactionResponseDto);

            result.add(salaryTransactionResponseDto);
        }

        return result;
    }



    private BigDecimal calculateProratedComponent(StaffSalaryComponent type,
                                                  List<SalaryComponent> changes,
                                                  BigDecimal basicPay,
                                                  LocalDate monthStart,
                                                  LocalDate monthEnd,
                                                  LocalDate relievedDate) {

        if (changes.isEmpty()) return BigDecimal.ZERO;

        if (relievedDate != null && !relievedDate.isAfter(monthEnd)) {
            monthEnd = relievedDate.minusDays(1);
        }

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < changes.size(); i++) {
            SalaryComponent current = changes.get(i);
            LocalDate rangeStart = current.getEffectiveDate().isBefore(monthStart) ? monthStart : current.getEffectiveDate();
            LocalDate rangeEnd = (i + 1 < changes.size()) ? changes.get(i + 1).getEffectiveDate().minusDays(1) : monthEnd;

            if (rangeEnd.isBefore(rangeStart)) continue;

            long days = rangeEnd.toEpochDay() - rangeStart.toEpochDay() + 1;
            long totalDays = monthEnd.toEpochDay() - monthStart.toEpochDay() + 1;
            BigDecimal daysRatio = BigDecimal.valueOf(days)
                    .divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_UP);

            BigDecimal amount = (current.getPercentage() != null)
                    ? basicPay
                    .multiply(current.getPercentage()
                            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                    .multiply(daysRatio)
                    : (current.getFixedAmount() != null ? current.getFixedAmount().multiply(daysRatio) : BigDecimal.ZERO);

            total = total.add(amount);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }



    private boolean isDeduction(StaffSalaryComponent comp) {
        return switch (comp) {
            case PF, ESI, PT, TDS, OTHER_DEDUCTIONS -> true;
            default -> false;
        };
    }




    @Override
    public List<SalaryTransactionResponseDto> getAllByMonth(YearMonth month) {
        return salaryTransactionRepository.findBySalaryMonth(month.atDay(1))
                .stream()
                .map(salaryTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryTransactionResponseDto> getAllBetweenMonths(YearMonth start, YearMonth end) {
        return salaryTransactionRepository.findBySalaryMonthBetweenOrderBySalaryMonthAsc(start.atDay(1), end.atDay(1))
                .stream()
                .map(salaryTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryTransactionResponseDto> getAllByStaff(Long staffId) {
        return salaryTransactionRepository.findByStaffIdOrderBySalaryMonthAsc(staffId)
                .stream()
                .map(salaryTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalaryTransactionResponseDto getByStaffAndMonth(Long staffId, YearMonth month) {
        return salaryTransactionRepository.findByStaffIdAndSalaryMonth(staffId, month.atDay(1))
                .map(salaryTransactionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

    }

    @Override
    public List<SalaryTransactionResponseDto> getLatestMonthSalaries() {
        return salaryTransactionRepository.findAllLatest()
                .stream()
                .map(salaryTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalaryTransactionResponseDto getLatestSalaryForStaff(Long staffId) {
        return salaryTransactionRepository.findLatestByStaffId(staffId)
                .map(salaryTransactionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("No latest salary found"));

    }


}
