package com.example.salaryAquittance.utility;

import com.example.salaryAquittance.service.SalaryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class SalaryGenerator {
    private final SalaryTransactionService salaryTransactionService;

//    @Scheduled(initialDelay = 1000*60*10, fixedRate = 10000*60*10)
    public void generate(){
        YearMonth currentMonth = YearMonth.now();
        salaryTransactionService.generateSalaryForAllStaff(currentMonth);
    }
}
