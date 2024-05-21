package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.response.TransactionReport;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.repository.OrderRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private OrderRepository orderRepository;

    public byte[] generateTransactionReport(Date startDate, Date endDate){
        try {
            List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
            List<TransactionReport> transactionReports = convertOrderToTransactionReports(orders);

            File file = ResourceUtils.getFile("src/main/resources/templates/OrderReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            //Set report data
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactionReports);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Order Report");

            //Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

            return reportContent;
        }catch (JRException e){
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TransactionReport> convertOrderToTransactionReports(List<Order> orders){
        return orders.stream().map(
                order -> new TransactionReport(
                        order.getCode(),
                        order.getOrderDate(),
                        order.getCustomer().getName(),
                        order.getItem().getName(),
                        order.getItem().getPrice(),
                        order.getQuantity(),
                        order.getTotalPrice()
                )
        ).collect(Collectors.toList());
    }
}
