package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.request.CustomerRequest;
import com.example.padepokanshop.shop.dto.response.CustomerResponse;
import com.example.padepokanshop.shop.model.Customer;
import com.example.padepokanshop.shop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final ImageStorageService imageStorageService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ImageStorageService imageStorageService){
        this.customerRepository = customerRepository;
        this.imageStorageService = imageStorageService;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id){
        return Optional.ofNullable(customerRepository.findById(id).orElseThrow(
                ()
                        -> new RuntimeException("Customer with ID" + id + "Not Found")));
    }

    public CustomerResponse createCustomer(CustomerRequest request, MultipartFile imageFile){
        try{
            String imageUrl = imageStorageService.uploadImage(imageFile);

            Customer customer = new Customer();
            customer.setCode(generateCustomerCode());
            customer.setName(request.getName());
            customer.setPhone(request.getPhone());
            customer.setAddress(request.getAddress());
            customer.setImageUrl(imageUrl);
            customer.setIsActive(true);

            Customer savedCustomer = customerRepository.save(customer);
            return new CustomerResponse(
                    savedCustomer.getCode(),
                    savedCustomer.getName(),
                    savedCustomer.getPhone(),
                    savedCustomer.getAddress(),
                    savedCustomer.getImageUrl(),
                    savedCustomer.getIsActive(),
                    savedCustomer.getLastOrderDate()
            );
        }catch (Exception e){
            throw new RuntimeException("Failed to save customer");
        }
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()){
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(request.getName());
            existingCustomer.setPhone(request.getPhone());
            existingCustomer.setAddress(request.getAddress());
            existingCustomer.setImageUrl(request.getImageUrl());
            existingCustomer.setIsActive(request.getIsActive());

            Customer updatedCustomer = customerRepository.save(existingCustomer);

            return new CustomerResponse(
                    updatedCustomer.getName(),
                    updatedCustomer.getPhone(),
                    updatedCustomer.getAddress(),
                    updatedCustomer.getCode(),
                    updatedCustomer.getImageUrl(),
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

    private String generateCustomerCode() {
        String prefix = "C-";
        Optional<String> lastCodeOptional = customerRepository.findLastCustomerCode(prefix);

        int newCodeNumber = 1;
        if (lastCodeOptional.isPresent()) {
            String lastCode = lastCodeOptional.get();
            String[] parts = lastCode.split("-");
            if (parts.length == 2) {
                try {
                    newCodeNumber = Integer.parseInt(parts[1]) + 1;
                } catch (NumberFormatException e) {
                    // Handle error appropriately
                }
            }
        }

        return prefix + String.format("%05d", newCodeNumber);
    }

}
