package com.example.customer_management.service.impl;

import com.example.customer_management.dto.CustomerRequest;
import com.example.customer_management.dto.CustomerResponse;
import com.example.customer_management.model.Customer;
import com.example.customer_management.model.Tier;
import com.example.customer_management.repository.CustomerRepository;
import com.example.customer_management.service.CustomerService;
import com.example.customer_management.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    @Transactional
    public CustomerResponse createCustomer(@Valid CustomerRequest request) {
        validateCustomerRequest(request, null);
        
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }
    
    @Override
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return convertToResponse(customer);
    }
    
    @Override
    public List<CustomerResponse> getCustomersByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customers.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return convertToResponse(customer);
    }
    
    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CustomerResponse updateCustomer(UUID id, @Valid CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        validateCustomerRequest(request, id);
        
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponse(updatedCustomer);
    }
    
    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
    
    @Override
    public void validateCustomerRequest(CustomerRequest request, UUID existingCustomerId) {
        // Check if email already exists (excluding current customer for updates)
        if (existingCustomerId == null) {
            // For create operation
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + request.getEmail());
            }
        } else {
            // For update operation
            Customer existingCustomerWithEmail = customerRepository.findByEmail(request.getEmail()).orElse(null);
            if (existingCustomerWithEmail != null && !existingCustomerWithEmail.getId().equals(existingCustomerId)) {
                throw new IllegalArgumentException("Email already exists for another customer: " + request.getEmail());
            }
        }
    }
    
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setAnnualSpend(customer.getAnnualSpend());
        response.setLastPurchaseDate(customer.getLastPurchaseDate());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        response.setTier(calculateTier(customer));
        return response;
    }
    
    private Tier calculateTier(Customer customer) {
        Double annualSpend = customer.getAnnualSpend();
        LocalDate lastPurchaseDate = customer.getLastPurchaseDate();
        
        if (annualSpend == null) {
            return Tier.BRONZE;
        }
        
        if (annualSpend < 1000) {
            return Tier.SILVER;
        } else if (annualSpend >= 1000 && annualSpend < 10000) {
            return Tier.GOLD;
        } else if (annualSpend >= 10000) {
            // Check for Platinum tier requirements
            if (lastPurchaseDate != null) {
                long monthsSinceLastPurchase = ChronoUnit.MONTHS.between(
                    lastPurchaseDate, LocalDate.now());
                if (monthsSinceLastPurchase <= 6) {
                    return Tier.PLATINUM;
                }
            }
            // Check for Gold tier requirements (with purchase within 12 months)
            if (lastPurchaseDate != null) {
                long monthsSinceLastPurchase = ChronoUnit.MONTHS.between(
                    lastPurchaseDate, LocalDate.now());
                if (monthsSinceLastPurchase <= 12) {
                    return Tier.GOLD;
                }
            }
            // If no recent purchase, fall back to Silver
            return Tier.SILVER;
        }
        
        return Tier.BRONZE;
    }

	
 // For testing purposes only
    Tier calculateTierForTest(Customer customer) {
        return calculateTier(customer);
    }
}