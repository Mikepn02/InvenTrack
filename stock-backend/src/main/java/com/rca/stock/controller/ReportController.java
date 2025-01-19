package com.rca.stock.controller;


import com.rca.stock.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("report")
public class ReportController {

    private final ReportService service;

    @GetMapping("/stock-levels")
    public ResponseEntity<?> getStockLevelReports() {
        try {
            byte[] report = service.generateStockLevelReports();
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=stock_level_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(report);
        } catch (Exception e) {
            String errorMessage = "Failed to generate stock level report: " + e.getMessage();
            String sanitizedMessage = errorMessage.replaceAll("[\r\n]", " ");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(sanitizedMessage);
        }
    }


    @GetMapping("/order-history")
    public ResponseEntity<?> getOrderHistoryReports(@RequestParam String startDateStr, @RequestParam String endDateStr) {
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = convertStringToLocalDate(startDateStr);
            endDate = convertStringToLocalDate(endDateStr);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
        }

        try {
            byte[] report = service.generateOrderHistoryReports(startDate, endDate);
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=order_history_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(report);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Error while generating report: " + e.getMessage());
        }
    }


    @GetMapping("/supplier-performance")
    public ResponseEntity<byte[]> getSupplierPerformanceReport(@RequestParam Integer supplierId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            byte[] report = service.generateSupplierPerformanceReports(supplierId, startDate, endDate);
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=supplier_performance_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(report);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all-supplier-performance")
    public ResponseEntity<byte[]> getAllSupplierPerformanceReports(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try{
            byte[] report = service.generateAllSupplierPerformanceReports(startDate, endDate);
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=all_supplier_performance_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(report);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/order-history-excel")
    public ResponseEntity<byte[]> getOrderHistoryReportsExcel(@RequestParam String startDateStr, @RequestParam String endDateStr) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = convertStringToLocalDate(startDateStr);
            endDate = convertStringToLocalDate(endDateStr);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            byte[] report = service.generateOrderHistoryReportsExcel(startDate, endDate);
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=order_history_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private LocalDate convertStringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }
}
