package com.example.customer_management.service;

import com.example.customer_management.dto.CustomerRequest;
import com.example.customer_management.dto.CustomerResponse;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(UUID id);
    List<CustomerResponse> getCustomersByName(String name);
    CustomerResponse getCustomerByEmail(String email);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(UUID id, CustomerRequest request);
    void deleteCustomer(UUID id);
    void validateCustomerRequest(CustomerRequest request, UUID existingCustomerId);
}