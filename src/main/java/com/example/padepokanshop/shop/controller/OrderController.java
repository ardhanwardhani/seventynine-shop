package com.example.padepokanshop.shop.controller;

import com.example.padepokanshop.shop.dto.request.OrderCreateRequest;
import com.example.padepokanshop.shop.dto.request.OrderItemRequest;
import com.example.padepokanshop.shop.dto.request.OrderUpdateRequest;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.service.OrderService;
import com.example.padepokanshop.shop.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/list")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        try{
            return orderService.getOrderById(id).map(ResponseEntity::ok).orElse(
                    ResponseEntity.notFound().build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBulkOrders(@RequestBody OrderCreateRequest request){
        try{
            List<OrderItemRequest> orderItems = request.getItems();
            List<Order> createOrders = orderService.createBulkOrders(orderItems, request.getCustomerId());
            return ResponseEntity.ok(createOrders);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequest request){
        if (orderService.getOrderById(orderId).isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Order updateOrder = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id){
        if(orderService.getOrderById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete order");
        }
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

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/pdf");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=transactionReport.pdf");
//        return ResponseEntity.ok().headers(headers).body(reportContent);
    }
}