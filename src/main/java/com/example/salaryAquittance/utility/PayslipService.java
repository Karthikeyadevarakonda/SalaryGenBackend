package com.example.salaryAquittance.utility;

import com.example.salaryAquittance.dto.salaryDetailsDto.SalaryDetailsResponseDto;
import com.example.salaryAquittance.dto.staffDto.StaffResponseDto;
import com.example.salaryAquittance.dto.salaryTransactionDto.SalaryTransactionResponseDto;

import com.example.salaryAquittance.service.SalaryTransactionService;
import com.example.salaryAquittance.service.StaffService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class PayslipService {

    private final StaffService staffService;
    private final SalaryTransactionService salaryTransactionService;

    public File generatePayslip(Long staffId, YearMonth salaryMonth) throws IOException {
        // Fetch staff and salary transaction
        StaffResponseDto staff = staffService.getStaffById(staffId);
        SalaryTransactionResponseDto transaction = salaryTransactionService
                .getByStaffAndMonth(staffId, salaryMonth);

        SalaryDetailsResponseDto salaryDetails = staff.getSalaryDetails();

        // File output path
        String fileName = "Payslip_" + staff.getName().replaceAll(" ", "_") + "_" + salaryMonth + ".pdf";
        File pdfFile = new File(fileName);

        // Create PDF
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        var font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);

        document.add(new Paragraph("SALARY SLIP")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Salary Month: " + salaryMonth).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        // Staff Info
        document.add(new Paragraph("Employee Details").setBold());
        document.add(new Paragraph("Name: " + staff.getName()));
        document.add(new Paragraph("Department: " + staff.getDepartment()));
        document.add(new Paragraph("Joining Date: " + staff.getJoiningDate()));
        document.add(new Paragraph("Relieved Date: " + (staff.getRelievedDate() != null ? staff.getRelievedDate() : "Still Working")));

        // Bank Info
        document.add(new Paragraph("\nBank Details").setBold());
        document.add(new Paragraph("Bank Name: " + salaryDetails.getBankName()));
        document.add(new Paragraph("Account Number: " + salaryDetails.getBankAccountNumber()));
        document.add(new Paragraph("IFSC Code: " + salaryDetails.getIfscCode()));

        // Salary Info
        document.add(new Paragraph("\nSalary Details").setBold());
        document.add(new Paragraph("Basic Pay: ₹" + format(transaction.getBasicPay())));
        document.add(new Paragraph("Gross Salary: ₹" + format(transaction.getGrossSalary())));
        document.add(new Paragraph("Total Deductions: ₹" + format(transaction.getTotalDeductions())));
        document.add(new Paragraph("Net Salary: ₹" + format(transaction.getNetSalary())));

        // Component Breakdown
        document.add(new Paragraph("\nComponent Breakdown").setBold());
        if (transaction.getComponentBreakdown() != null && !transaction.getComponentBreakdown().isEmpty()) {
            Table table = new Table(2);
            table.addCell(new Cell().add(new Paragraph("Component").setBold()));
            table.addCell(new Cell().add(new Paragraph("Amount").setBold()));


            transaction.getComponentBreakdown().forEach((component, amount) -> {
                table.addCell(component);
                table.addCell("₹" + format(amount));
            });

            document.add(table);
        }

        document.close();
        return pdfFile;
    }

    private String format(BigDecimal value) {
        return value != null ? value.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }
}
