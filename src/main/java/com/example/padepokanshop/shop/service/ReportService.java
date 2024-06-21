package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.response.OrderSummary;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.model.OrderItem;
import com.example.padepokanshop.shop.repository.OrderRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @Autowired
    private OrderService orderService;

    public byte[] generateTransactionReport(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
            List<OrderSummary> orderSummaries = orders.stream()
                    .map(this::mapToOrderSummaryDTO)
                    .toList();

            File file = ResourceUtils.getFile("src/main/resources/templates/OrderReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderSummaries);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Order Summaries Report");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private OrderSummary mapToOrderSummaryDTO(Order order) {
        OrderSummary dto = new OrderSummary();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderCode(order.getCode());
        dto.setCustomerName(order.getCustomer().getName());

        int totalQuantity = order.getOrderItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        dto.setTotalQuantity(totalQuantity);

        double totalAmount = order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getItem().getPrice())
                .sum();
        dto.setTotalAmount(totalAmount);

        return dto;
    }
}
