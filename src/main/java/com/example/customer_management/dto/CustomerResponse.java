package com.example.customer_management.dto;

import com.example.customer_management.model.Tier;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerResponse {
    
    @Schema(description = "Customer ID")
    private UUID id;
    
    @Schema(description = "Customer name")
    private String name;
    
    @Schema(description = "Customer email")
    private String email;
    
    @Schema(description = "Annual spend amount")
    private Double annualSpend;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Last purchase date")
    private LocalDate lastPurchaseDate;
    
    @Schema(description = "Calculated membership tier")
    private Tier tier;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Double getAnnualSpend() { return annualSpend; }
    public void setAnnualSpend(Double annualSpend) { this.annualSpend = annualSpend; }
    
    public LocalDate getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(LocalDate lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }
    
    public Tier getTier() { return tier; }
    public void setTier(Tier tier) { this.tier = tier; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}