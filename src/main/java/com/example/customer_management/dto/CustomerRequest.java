package com.example.customer_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public class CustomerRequest {
    
    @NotBlank(message = "Name is required")
    @Schema(description = "Customer name", example = "John Doe", required = true)
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Customer email", example = "john.doe@example.com", required = true)
    private String email;
    
    @Schema(description = "Annual spend amount", example = "5000.00")
    private Double annualSpend;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Last purchase date in ISO 8601 format", example = "2024-01-15")
    private LocalDate lastPurchaseDate;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Double getAnnualSpend() { return annualSpend; }
    public void setAnnualSpend(Double annualSpend) { this.annualSpend = annualSpend; }
    
    public LocalDate getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(LocalDate lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }
}