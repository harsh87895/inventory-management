package com.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @InjectMocks
    private OpenApiConfig openApiConfig;

    @Test
    void openAPI_ShouldReturnCorrectConfiguration() {
        OpenAPI openAPI = openApiConfig.openAPI();
        
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        
        Info info = openAPI.getInfo();
        assertEquals("Inventory Management System API", info.getTitle());
        assertEquals("RESTful API for managing product inventory, categories, and SKUs", info.getDescription());
        assertEquals("1.0.0", info.getVersion());
    }
} 