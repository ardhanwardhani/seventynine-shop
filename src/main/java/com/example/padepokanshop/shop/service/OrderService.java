package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.request.OrderCreateRequest;
import com.example.padepokanshop.shop.dto.request.OrderItemRequest;
import com.example.padepokanshop.shop.dto.request.OrderUpdateRequest;
import com.example.padepokanshop.shop.dto.response.OrderItemResponse;
import com.example.padepokanshop.shop.dto.response.OrderResponse;
import com.example.padepokanshop.shop.dto.response.OrderSummary;
import com.example.padepokanshop.shop.model.Customer;
import com.example.padepokanshop.shop.model.Item;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.model.OrderItem;
import com.example.padepokanshop.shop.repository.CustomerRepository;
import com.example.padepokanshop.shop.repository.ItemRepository;
import com.example.padepokanshop.shop.repository.OrderItemRepository;
import com.example.padepokanshop.shop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CustomerService customerService;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDTO);
    }

    private OrderResponse convertToDTO(Order order) {
        OrderResponse orderDTO = new OrderResponse();
        orderDTO.setOrderId(order.getId());
        orderDTO.setOrderCode(order.getCode());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setCustomerId(order.getCustomer().getId());
        orderDTO.setCustomerName(order.getCustomer().getName());
        orderDTO.setOrderItems(order.getOrderItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        return orderDTO;
    }

    private OrderItemResponse convertToDTO(OrderItem orderItem) {
        OrderItemResponse orderItemDTO = new OrderItemResponse();
        orderItemDTO.setOrderItemId(orderItem.getId());
        orderItemDTO.setItemId(orderItem.getItem().getId());
        orderItemDTO.setItemCode(orderItem.getItem().getCode());
        orderItemDTO.setItemName(orderItem.getItem().getName());
        orderItemDTO.setPrice(orderItem.getItem().getPrice());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        return orderItemDTO;
    }

    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(request.getCustomerId());
            if (existingCustomer.isPresent()){
                Order order = new Order();
                order.setCode(generateOrderCode());
                order.setOrderDate(new Date());
                order.setCustomer(existingCustomer.get());
                Order savedOrder = orderRepository.save(order);

                for (OrderItemRequest itemDTO : request.getItems()) {
                    Item item = itemRepository.findById(itemDTO.getItemId())
                            .orElseThrow(() -> new RuntimeException("Item not found"));

                    if (item.getStock() < itemDTO.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for item: " + item.getName());
                    }

                    // Kurangi stok item
                    item.setStock(item.getStock() - itemDTO.getQuantity());
                    itemRepository.save(item);

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setItem(item);
                    orderItem.setQuantity(itemDTO.getQuantity());
                    orderItemRepository.save(orderItem);
                }

                return savedOrder;
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found");
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Order updateOrder(Long orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        order.setCustomer(customer);

        List<OrderItem> oldOrderItems = order.getOrderItems();
        for (OrderItem oldOrderItem : oldOrderItems) {
            Item item = oldOrderItem.getItem();
            item.setStock(item.getStock() + oldOrderItem.getQuantity());
            itemRepository.save(item);
        }

        orderItemRepository.deleteAll(order.getOrderItems());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemDTO : request.getItems()) {
            Item item = itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (item.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Insufficient stock for item: " + item.getName());
            }

            item.setStock(item.getStock() - itemDTO.getQuantity());
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()){
            orderRepository.deleteById(orderId);
        }else {
            throw new RuntimeException("Order with ID " + orderId + "not found");
        }
    }

    public List<Order> getOrderByCustomerId(Long customerId){
        List<Order> optionalOrder = orderRepository.findOrderByCustomerId(customerId);
        return optionalOrder;
    }

    private String generateOrderCode() {
        String prefix = "TRX-";
        Optional<String> lastCodeOptional = orderRepository.findLastOrderCode(prefix);

        int newCodeNumber = 1;
        if (lastCodeOptional.isPresent()) {
            String lastCode = lastCodeOptional.get();
            String[] parts = lastCode.split("-");
            if (parts.length == 2) {
                try {
                    newCodeNumber = Integer.parseInt(parts[1]) + 1;
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Failed to generate code: ", e);
                }
            }
        }

        return prefix + String.format("%05d", newCodeNumber);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OrderSummary> getOrderSummaries() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderSummaryDTO)
                .collect(Collectors.toList());
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