package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.request.CustomerRequest;
import com.example.padepokanshop.shop.dto.response.CustomerResponse;
import com.example.padepokanshop.shop.model.Customer;
import com.example.padepokanshop.shop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id){
        return Optional.ofNullable(customerRepository.findById(id).orElseThrow(
                ()
                        -> new RuntimeException("Customer with ID" + id + "Not Found")));
    }

    public CustomerResponse createCustomer(CustomerRequest request){
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCode(request.getCode());
        customer.setPic(request.getPic());
        customer.setIsActive(true);

        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponse(
                savedCustomer.getName(),
                savedCustomer.getPhone(),
                savedCustomer.getAddress(),
                savedCustomer.getCode(),
                savedCustomer.getPic(),
                savedCustomer.getIsActive(),
                savedCustomer.getLastOrderDate()
        );
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()){
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(request.getName());
            existingCustomer.setPhone(request.getPhone());
            existingCustomer.setAddress(request.getAddress());
            existingCustomer.setCode(request.getCode());
            existingCustomer.setPic(request.getPic());
            existingCustomer.setIsActive(request.getIsActive());

            Customer updatedCustomer = customerRepository.save(existingCustomer);

            return new CustomerResponse(
                    updatedCustomer.getName(),
                    updatedCustomer.getPhone(),
                    updatedCustomer.getAddress(),
                    updatedCustomer.getCode(),
                    updatedCustomer.getPic(),
                    updatedCustomer.getIsActive(),
                    updatedCustomer.getLastOrderDate()
            );
        }else {
            throw new RuntimeException("Customer with ID" + id + "not found.");
        }
    }

    public void deleteCustomer(Long id){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            customerRepository.deleteById(id);
        }else {
            throw new RuntimeException("Customer with ID" + id + "not found");
        }
    }

}
