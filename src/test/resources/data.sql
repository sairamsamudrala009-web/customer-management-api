INSERT INTO customers (first_name, last_name, email, phone, annual_spend, membership_tier, created_at, updated_at) 
VALUES 
('John', 'Doe', 'john.doe@example.com', '+1234567890', 3000.00, 'BRONZE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane', 'Smith', 'jane.smith@example.com', '+0987654321', 8000.00, 'SILVER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob', 'Johnson', 'bob.johnson@example.com', '+1122334455', 20000.00, 'GOLD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alice', 'Williams', 'alice.williams@example.com', '+5566778899', 75000.00, 'PLATINUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);