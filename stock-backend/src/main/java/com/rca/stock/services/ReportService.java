package com.rca.stock.services;

import java.time.LocalDate;

public interface ReportService {

    byte[] generateStockLevelReports() throws Exception;
    byte[] generateOrderHistoryReports(LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] generateSupplierPerformanceReports(Integer supplierId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] generateAllSupplierPerformanceReports(LocalDate startDate , LocalDate endDate) throws Exception;
    byte[] generateOrderHistoryReportsExcel(LocalDate startDate, LocalDate endDate) throws Exception;
}
