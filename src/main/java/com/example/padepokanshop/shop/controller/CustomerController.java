package com.example.padepokanshop.shop.controller;

import com.example.padepokanshop.shop.dto.request.CustomerRequest;
import com.example.padepokanshop.shop.dto.response.CustomerResponse;
import com.example.padepokanshop.shop.model.Customer;
import com.example.padepokanshop.shop.service.CustomerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public ResponseEntity<Object> listCustomers(){
        try{
            List<Customer> customers = customerService.getAllCustomers();
            if (customers.isEmpty()){
                String message = "No customer found";
                return ResponseEntity.ok(message);
            }
            return ResponseEntity.ok(customers);
        }catch (Exception e){
            String errorMessage = "Error fetching customer";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        try{
            return customerService.getCustomerById(id).map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest request){
        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest request){
        try{
            CustomerResponse response = customerService.updateCustomer(id, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isEmpty()){
            return ResponseEntity.ok("Failed to delete customer");
        }

        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Deleted customer successfully");
    }
}
