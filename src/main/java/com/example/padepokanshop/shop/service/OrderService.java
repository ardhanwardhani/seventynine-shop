package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.request.OrderItemRequest;
import com.example.padepokanshop.shop.dto.request.OrderUpdateRequest;
import com.example.padepokanshop.shop.model.Customer;
import com.example.padepokanshop.shop.model.Item;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.repository.CustomerRepository;
import com.example.padepokanshop.shop.repository.ItemRepository;
import com.example.padepokanshop.shop.repository.OrderRepository;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomerService customerService;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id){
        return Optional.ofNullable(orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order with ID" + id + "not found")
        ));
    }

    public Order createOrder(OrderItemRequest request, Long customerId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new RuntimeException("Customer not found")
        );

        Item item = itemRepository.findById(request.getItemId()).orElseThrow(
                () -> new RuntimeException("Item not found")
        );

        if (item.getStock() < request.getQuantity()){
            throw new IllegalArgumentException("Insufficient stock for item " + item.getName());
        }

        item.setStock(item.getStock() - request.getQuantity());

        if (item.getStock() == 0){
            item.setIsAvailable(false);
        }

        itemRepository.save(item);

        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setItem(item);
        newOrder.setQuantity(request.getQuantity());
        newOrder.setTotalPrice(request.getQuantity() * item.getPrice());
        newOrder.setOrderDate(new Date());

        customer.setLastOrderDate(new Date());
        customerRepository.save(customer);

        return orderRepository.save(newOrder);
    }

    public List<Order> createBulkOrders(List<OrderItemRequest> requests, Long customerId){
        List<Order> listOrders = new ArrayList<>();

        for (OrderItemRequest request : requests){
            try{
                Order createdOrder = createOrder(request, customerId);
                listOrders.add(createdOrder);
            }catch (IllegalArgumentException e){
                rollbackOrders(listOrders);
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return listOrders;
    }

    private void rollbackOrders(List<Order> listOrders){
        for (Order order : listOrders){
            Item item = order.getItem();
            item.setStock(item.getStock() + order.getQuantity());
            itemRepository.save(item);
        }
    }

    public Order updateOrder(Long orderId, OrderUpdateRequest request){
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found")
        );

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new RuntimeException("Customer not found")
        );

        Item item = itemRepository.findById(request.getItemId()).orElseThrow(
                () -> new RuntimeException("Item not found")
        );

        existingOrder.setCustomer(customer);
        existingOrder.setItem(item);
        existingOrder.setQuantity(request.getQuantity());

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()){
            orderRepository.deleteById(orderId);
        }else {
            throw new RuntimeException("Order with ID " + orderId + "not found");
        }
    }
}