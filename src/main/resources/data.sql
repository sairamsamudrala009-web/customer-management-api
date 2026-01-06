/*INSERT INTO customers (id, name, email, annual_spend, last_purchase_date) 
VALUES 
    ('101', 'sairam', 'sairam@example.com', 500.0, '2024-01-15T10:30:00'),
    ('102', 'samudrala', 'samudrala@gmail.com', 3000.0, '2024-06-20T14:45:00'),
    ('103', 'ravindra', 'ravindra@gmail.com', 15000.0, '2024-11-01T09:15:00'),
    ('104', 'mahesh', 'mahesh@gmail.com', 800.0, '2023-12-10T16:20:00'),
    ('105', 'suresh', 'suresh@gmail.com', 12000.0, '2024-09-05T11:00:00');*/

INSERT INTO customers (first_name, last_name, email, phone, annual_spend, membership_tier, created_at, updated_at) 
VALUES 
('John', 'Doe', 'john.doe@example.com', '+1234567890', 3000.00, 'BRONZE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane', 'Smith', 'jane.smith@example.com', '+0987654321', 8000.00, 'SILVER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob', 'Johnson', 'bob.johnson@example.com', '+1122334455', 20000.00, 'GOLD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alice', 'Williams', 'alice.williams@example.com', '+5566778899', 75000.00, 'PLATINUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);