package com.example.customer_management.service;

import com.example.customer_management.dto.CustomerRequest;
import com.example.customer_management.dto.CustomerResponse;
import com.example.customer_management.model.Customer;
import com.example.customer_management.model.Tier;
import com.example.customer_management.repository.CustomerRepository;
import com.example.customer_management.service.impl.CustomerServiceImpl;
import com.example.customer_management.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private CustomerServiceImpl customerService;
    
    private CustomerRequest validRequest;
    private Customer customer;
    private UUID customerId;
    
    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        
        validRequest = new CustomerRequest();
        validRequest.setName("John Doe");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setAnnualSpend(5000.0);
        validRequest.setLastPurchaseDate(LocalDate.now().minusMonths(3));
        
        customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAnnualSpend(5000.0);
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(3));
    }
    
    @Test
    void createCustomer_ValidRequest_ReturnsCustomerResponse() {
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        CustomerResponse response = customerService.createCustomer(validRequest);
        
        assertNotNull(response);
        assertEquals(customerId, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals(5000.0, response.getAnnualSpend());
        assertEquals(Tier.GOLD, response.getTier());
        
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    
    @Test
    void getCustomerById_ExistingId_ReturnsCustomerResponse() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        
        CustomerResponse response = customerService.getCustomerById(customerId);
        
        assertNotNull(response);
        assertEquals(customerId, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals(Tier.GOLD, response.getTier());
        
        verify(customerRepository, times(1)).findById(customerId);
    }
    
    @Test
    void getCustomerById_NonExistingId_ThrowsException() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(customerId);
        });
        
        verify(customerRepository, times(1)).findById(customerId);
    }
    
    @Test
    void updateCustomer_ValidRequest_ReturnsUpdatedResponse() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setName("Jane Doe");
        updateRequest.setEmail("jane.doe@example.com");
        updateRequest.setAnnualSpend(15000.0);
        updateRequest.setLastPurchaseDate(LocalDate.now().minusMonths(2));
        
        CustomerResponse response = customerService.updateCustomer(customerId, updateRequest);
        
        assertNotNull(response);
        assertEquals("Jane Doe", response.getName());
        assertEquals("jane.doe@example.com", response.getEmail());
        assertEquals(15000.0, response.getAnnualSpend());
        assertEquals(Tier.PLATINUM, response.getTier());
        
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    
    @Test
    void deleteCustomer_ExistingId_DeletesSuccessfully() {
        when(customerRepository.existsById(customerId)).thenReturn(true);
        
        customerService.deleteCustomer(customerId);
        
        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, times(1)).deleteById(customerId);
    }
    
    @Test
    void calculateTier_SilverTier() {
        // Create a customer with Silver tier criteria
        Customer silverCustomer = new Customer();
        silverCustomer.setAnnualSpend(500.0);
        silverCustomer.setLastPurchaseDate(LocalDate.now().minusMonths(2));
        
        // Use the service to get response which includes tier calculation
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(silverCustomer);
        
        CustomerRequest silverRequest = new CustomerRequest();
        silverRequest.setName("Silver Customer");
        silverRequest.setEmail("silver@example.com");
        silverRequest.setAnnualSpend(500.0);
        silverRequest.setLastPurchaseDate(LocalDate.now().minusMonths(2));
        
        CustomerResponse response = customerService.createCustomer(silverRequest);
        
        assertEquals(Tier.SILVER, response.getTier());
    }
    
    @Test
    void calculateTier_GoldTier() {
        // Create a customer with Gold tier criteria
        Customer goldCustomer = new Customer();
        goldCustomer.setAnnualSpend(5000.0);
        goldCustomer.setLastPurchaseDate(LocalDate.now().minusMonths(8));
        
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(goldCustomer);
        
        CustomerRequest goldRequest = new CustomerRequest();
        goldRequest.setName("Gold Customer");
        goldRequest.setEmail("gold@example.com");
        goldRequest.setAnnualSpend(5000.0);
        goldRequest.setLastPurchaseDate(LocalDate.now().minusMonths(8));
        
        CustomerResponse response = customerService.createCustomer(goldRequest);
        
        assertEquals(Tier.GOLD, response.getTier());
    }
    
    @Test
    void calculateTier_PlatinumTier() {
        // Create a customer with Platinum tier criteria
        Customer platinumCustomer = new Customer();
        platinumCustomer.setAnnualSpend(15000.0);
        platinumCustomer.setLastPurchaseDate(LocalDate.now().minusMonths(2));
        
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(platinumCustomer);
        
        CustomerRequest platinumRequest = new CustomerRequest();
        platinumRequest.setName("Platinum Customer");
        platinumRequest.setEmail("platinum@example.com");
        platinumRequest.setAnnualSpend(15000.0);
        platinumRequest.setLastPurchaseDate(LocalDate.now().minusMonths(2));
        
        CustomerResponse response = customerService.createCustomer(platinumRequest);
        
        assertEquals(Tier.PLATINUM, response.getTier());
    }
    
    @Test
    void calculateTier_BronzeTier() {
        // Create a customer with Bronze tier criteria (no annual spend)
        Customer bronzeCustomer = new Customer();
        bronzeCustomer.setAnnualSpend(null);
        
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(bronzeCustomer);
        
        CustomerRequest bronzeRequest = new CustomerRequest();
        bronzeRequest.setName("Bronze Customer");
        bronzeRequest.setEmail("bronze@example.com");
        bronzeRequest.setAnnualSpend(null);
        bronzeRequest.setLastPurchaseDate(null);
        
        CustomerResponse response = customerService.createCustomer(bronzeRequest);
        
        assertEquals(Tier.BRONZE, response.getTier());
    }
    
    @Test
    void calculateTier_HighSpendButNoRecentPurchase() {
        // Create a customer with high spend but no recent purchase
        Customer customer = new Customer();
        customer.setAnnualSpend(15000.0);
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(18));
        
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        CustomerRequest request = new CustomerRequest();
        request.setName("High Spender");
        request.setEmail("high@example.com");
        request.setAnnualSpend(15000.0);
        request.setLastPurchaseDate(LocalDate.now().minusMonths(18));
        
        CustomerResponse response = customerService.createCustomer(request);
        
        assertEquals(Tier.SILVER, response.getTier());
    }
    
    @Test
    void calculateTier_GoldWithRecentPurchase() {
        // Customer with high spend but purchase within 12 months (but not 6)
        Customer customer = new Customer();
        customer.setAnnualSpend(12000.0);
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(8));
        
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        CustomerRequest request = new CustomerRequest();
        request.setName("Recent Gold");
        request.setEmail("recentgold@example.com");
        request.setAnnualSpend(12000.0);
        request.setLastPurchaseDate(LocalDate.now().minusMonths(8));
        
        CustomerResponse response = customerService.createCustomer(request);
        
        assertEquals(Tier.GOLD, response.getTier());
    }
    
    @Test
    void createCustomer_DuplicateEmail_ThrowsException() {
        when(customerRepository.existsByEmail(any())).thenReturn(true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(validRequest);
        });
        
        verify(customerRepository, times(1)).existsByEmail(any());
        verify(customerRepository, never()).save(any(Customer.class));
    }
    
    @Test
    void updateCustomer_DuplicateEmailForAnotherCustomer_ThrowsException() {
        UUID anotherCustomerId = UUID.randomUUID();
        
        // Setup existing customer with ID we're trying to update
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        
        // Setup another customer with the email we're trying to update to
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(anotherCustomerId);
        anotherCustomer.setEmail("existing@example.com");
        anotherCustomer.setName("Existing Customer");
        
        // Mock findByEmail to return another customer
        when(customerRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(anotherCustomer));
        
        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("existing@example.com"); // Same as another customer
        updateRequest.setAnnualSpend(1000.0);
        updateRequest.setLastPurchaseDate(LocalDate.now().minusMonths(1));
        
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.updateCustomer(customerId, updateRequest);
        });
        
        verify(customerRepository, times(1)).findByEmail("existing@example.com");
        verify(customerRepository, never()).save(any(Customer.class));
    }
    
    @Test
    void updateCustomer_SameEmailForSameCustomer_Success() {
        // Customer wants to keep their same email
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.doe@example.com"); // Same email
        updateRequest.setAnnualSpend(2000.0);
        updateRequest.setLastPurchaseDate(LocalDate.now().minusMonths(1));
        
        CustomerResponse response = customerService.updateCustomer(customerId, updateRequest);
        
        assertNotNull(response);
        assertEquals("John Updated", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
        
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    
    @Test
    void updateCustomer_NewEmailNotInUse_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("new.email@example.com"); // New email
        updateRequest.setAnnualSpend(2000.0);
        updateRequest.setLastPurchaseDate(LocalDate.now().minusMonths(1));
        
        CustomerResponse response = customerService.updateCustomer(customerId, updateRequest);
        
        assertNotNull(response);
        assertEquals("new.email@example.com", response.getEmail());
        
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}