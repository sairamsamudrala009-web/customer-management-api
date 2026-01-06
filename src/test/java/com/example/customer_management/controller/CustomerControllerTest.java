package com.example.customer_management.controller;

import com.example.customer_management.dto.CustomerRequest;
import com.example.customer_management.dto.CustomerResponse;
import com.example.customer_management.model.Tier;
import com.example.customer_management.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CustomerService customerService;
    
    private CustomerRequest validRequest;
    private CustomerResponse customerResponse;
    private UUID customerId;
    
    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        
        validRequest = new CustomerRequest();
        validRequest.setName("John Doe");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setAnnualSpend(5000.0);
        validRequest.setLastPurchaseDate(LocalDate.now().minusMonths(3));
        
        customerResponse = new CustomerResponse();
        customerResponse.setId(customerId);
        customerResponse.setName("John Doe");
        customerResponse.setEmail("john.doe@example.com");
        customerResponse.setAnnualSpend(5000.0);
        customerResponse.setLastPurchaseDate(LocalDate.now().minusMonths(3));
        customerResponse.setTier(Tier.GOLD);
        customerResponse.setCreatedAt(LocalDateTime.now());
        customerResponse.setUpdatedAt(LocalDateTime.now());
    }
    
    @Test
    void createCustomer_ValidRequest_ReturnsCreated() throws Exception {
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(customerResponse);
        
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.tier").value("GOLD"));
        
        verify(customerService, times(1)).createCustomer(any(CustomerRequest.class));
    }
    
    @Test
    void createCustomer_InvalidRequest_ReturnsBadRequest() throws Exception {
        CustomerRequest invalidRequest = new CustomerRequest();
        invalidRequest.setName("");
        invalidRequest.setEmail("invalid-email");
        
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }
    
    @Test
    void getCustomerById_ExistingId_ReturnsCustomer() throws Exception {
        when(customerService.getCustomerById(customerId)).thenReturn(customerResponse);
        
        mockMvc.perform(get("/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.tier").value("GOLD"));
        
        verify(customerService, times(1)).getCustomerById(customerId);
    }
    
    @Test
    void getCustomersByName_ReturnsCustomers() throws Exception {
        when(customerService.getCustomersByName("John")).thenReturn(List.of(customerResponse));
        
        mockMvc.perform(get("/customers")
                .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].tier").value("GOLD"));
        
        verify(customerService, times(1)).getCustomersByName("John");
    }
    
    @Test
    void updateCustomer_ValidRequest_ReturnsUpdatedCustomer() throws Exception {
        when(customerService.updateCustomer(eq(customerId), any(CustomerRequest.class)))
                .thenReturn(customerResponse);
        
        mockMvc.perform(put("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
        
        verify(customerService, times(1)).updateCustomer(eq(customerId), any(CustomerRequest.class));
    }
    
    @Test
    void deleteCustomer_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(customerId);
        
        mockMvc.perform(delete("/customers/{id}", customerId))
                .andExpect(status().isNoContent());
        
        verify(customerService, times(1)).deleteCustomer(customerId);
    }
}