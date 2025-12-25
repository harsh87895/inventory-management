package com.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventoryManagementApplicationTest {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
        InventoryManagementApplication.main(new String[]{});
    }
} 