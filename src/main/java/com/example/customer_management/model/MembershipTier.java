/*
 * package com.example.customer_management.model;
 * 
 * public enum MembershipTier { BRONZE(0, 5000, "Bronze - Basic tier"),
 * SILVER(5000, 15000, "Silver - Mid tier"), GOLD(15000, 50000,
 * "Gold - Premium tier"), PLATINUM(50000, Double.MAX_VALUE,
 * "Platinum - VIP tier");
 * 
 * private final double minSpend; private final double maxSpend; private final
 * String description;
 * 
 * MembershipTier(double minSpend, double maxSpend, String description) {
 * this.minSpend = minSpend; this.maxSpend = maxSpend; this.description =
 * description; }
 * 
 * public static MembershipTier fromAnnualSpend(double annualSpend) { for
 * (MembershipTier tier : values()) { if (annualSpend >= tier.minSpend &&
 * annualSpend < tier.maxSpend) { return tier; } } return BRONZE; }
 * 
 * public String getDescription() { return description; } }
 */