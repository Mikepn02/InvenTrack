package com.rca.stock.services.impl;

import com.rca.stock.dto.order.OrderReportDto;
import com.rca.stock.model.Order;
import com.rca.stock.model.StockItem;
import com.rca.stock.model.Supplier;
import com.rca.stock.repository.OrderRepository;
import com.rca.stock.repository.StockItemRepository;
import com.rca.stock.repository.SupplierRepository;
import com.rca.stock.services.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final StockItemRepository stockItemRepository;
    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public byte[] generateStockLevelReports() throws Exception {
        List<StockItem> stockItems = stockItemRepository.findAll();

        InputStream reportStream = getClass().getResourceAsStream("/reports/stock_level_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stockItems);
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateOrderHistoryReports(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Order> orders = orderRepository.findByDateBetween(startDate, endDate);
        List<OrderReportDto> reportData = orders.stream()
                .map(order -> new OrderReportDto(
                        order.getId(),
                        order.getDate(),
                        order.getStockItem().getPrice(),
                        order.getSupplier().getCompanyName()
                ))
                .collect(Collectors.toList());
        reportData.forEach(dto -> System.out.println(dto));



        InputStream reportStream = getClass().getResourceAsStream("/reports/order_history_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateSupplierPerformanceReports(Integer supplierId, LocalDate startDate, LocalDate endDate) throws Exception {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        List<Order>  orders = orderRepository.findByDateBetween(startDate , endDate)
                .stream().filter(order -> order.getSupplier().getId().equals(supplierId))
                .collect(Collectors.toList());

        InputStream reportStream = getClass().getResourceAsStream("/reports/supplier_performance_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("supplierName", supplier.getCompanyName());
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateAllSupplierPerformanceReports(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Order> orders = orderRepository.findByDateBetween(startDate, endDate);


        Map<Supplier, List<Order>> supplierOrdersMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getSupplier));

        InputStream reportStream = getClass().getResourceAsStream("/reports/all_supplier_performance_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        List<Map<String, Object>> reportData = suppliers.stream().map(supplier -> {
            List<Order> supplierOrders = supplierOrdersMap.getOrDefault(supplier, List.of());
            double averageDeliveryTime = 7.0;

            Map<String, Object> data = new HashMap<>();
            data.put("supplierName", supplier.getCompanyName());
            data.put("numOrders", supplierOrders.size());
            data.put("avgDeliveryTime", averageDeliveryTime);
            return data;
        }).collect(Collectors.toList());

        System.out.println("reportData: " + reportData);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", java.sql.Date.valueOf(startDate));
        parameters.put("endDate", java.sql.Date.valueOf(endDate));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


//    private double calculateAverageDeliveryTime(List<Order> orders) {
//        return orders.stream()
//                .mapToDouble(Order::getDeliveryTime)
//                .average()
//                .orElse(0.0);
//    }
    @Override
    public byte[] generateOrderHistoryReportsExcel(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Order> orders = orderRepository.findByDateBetween(startDate, endDate);
        List<OrderReportDto> reportData = orders.stream()
                .map(order -> new OrderReportDto(
                        order.getId(),
                        order.getDate(),
                        order.getStockItem().getPrice(),
                        order.getSupplier().getCompanyName()
                ))
                .collect(Collectors.toList());

        InputStream reportStream = getClass().getResourceAsStream("/reports/order_history_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[]{"Order History"});
        reportConfig.setDetectCellType(true);
        reportConfig.setWhitePageBackground(false);
        reportConfig.setIgnorePageMargins(true);

        exporter.setConfiguration(reportConfig);
        exporter.exportReport();

        return outputStream.toByteArray();
    }

}
