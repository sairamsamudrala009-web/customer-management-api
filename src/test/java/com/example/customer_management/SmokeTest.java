package com.example.customer_management;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {
    
    @Test
    void smokeTest() {
        System.out.println("Smoke test running...");
        assertThat(1 + 1).isEqualTo(2);
    }
}