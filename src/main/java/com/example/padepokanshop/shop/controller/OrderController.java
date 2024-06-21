package com.example.padepokanshop.shop.controller;

import com.example.padepokanshop.shop.dto.request.OrderCreateRequest;
import com.example.padepokanshop.shop.dto.request.OrderUpdateRequest;
import com.example.padepokanshop.shop.dto.response.OrderResponse;
import com.example.padepokanshop.shop.dto.response.OrderSummary;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.service.OrderService;
import com.example.padepokanshop.shop.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/list")
    public ResponseEntity<List<OrderSummary>> getOrderSummaries() {
        List<OrderSummary> orderSummaries = orderService.getOrderSummaries();
        return ResponseEntity.ok(orderSummaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        try {
            return orderService.getOrderById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        Order order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@Valid @PathVariable Long id, @RequestBody OrderUpdateRequest request){
        Order updateOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Deleted order succesfully");
    }

    @GetMapping("/report")
    public ResponseEntity<Resource> generateTransactionReport(
            @RequestParam("startDate")
                @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate")
                @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
            ){
        byte[] reportContent = reportService.generateTransactionReport(startDate, endDate);

        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("weekly-report.pdf")
                                .build().toString())
                .body(resource);
    }
}