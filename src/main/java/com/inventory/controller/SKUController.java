package com.inventory.controller;

import com.inventory.dto.request.SKURequest;
import com.inventory.dto.response.SKUResponse;
import com.inventory.service.SKUService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/skus")
@RequiredArgsConstructor
@Validated
@Tag(name = "SKU Management", description = "APIs for managing SKUs (Stock Keeping Units)")
public class SKUController {

    private final SKUService skuService;

    @PostMapping
    @Operation(summary = "Add a new SKU to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "SKU created successfully",
                    content = @Content(schema = @Schema(implementation = SKUResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - Validation failed",
                    content = @Content(schema = @Schema(ref = "ErrorResponse"))),
        @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(ref = "ErrorResponse"))),
        @ApiResponse(responseCode = "409", description = "SKU with same color and size already exists for the product",
                    content = @Content(schema = @Schema(ref = "ErrorResponse")))
    })
    public ResponseEntity<SKUResponse> createSKU(
            @Valid @RequestBody SKURequest request) {
        return new ResponseEntity<>(skuService.createSKU(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing SKU")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SKU updated successfully",
                    content = @Content(schema = @Schema(implementation = SKUResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - Validation failed or invalid price reduction",
                    content = @Content(schema = @Schema(ref = "ErrorResponse"))),
        @ApiResponse(responseCode = "404", description = "SKU or Product not found",
                    content = @Content(schema = @Schema(ref = "ErrorResponse"))),
        @ApiResponse(responseCode = "409", description = "SKU with same color and size already exists for the product",
                    content = @Content(schema = @Schema(ref = "ErrorResponse")))
    })
    public ResponseEntity<SKUResponse> updateSKU(
            @Parameter(description = "SKU ID", required = true)
            @PathVariable @Positive(message = "SKU ID must be positive") Long id,
            @Valid @RequestBody SKURequest request) {
        return ResponseEntity.ok(skuService.updateSKU(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a SKU by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SKU found",
                    content = @Content(schema = @Schema(implementation = SKUResponse.class))),
        @ApiResponse(responseCode = "404", description = "SKU not found",
                    content = @Content(schema = @Schema(ref = "ErrorResponse")))
    })
    public ResponseEntity<SKUResponse> getSKUById(
            @Parameter(description = "SKU ID", required = true)
            @PathVariable @Positive(message = "SKU ID must be positive") Long id) {
        return ResponseEntity.ok(skuService.getSKUById(id));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all SKUs for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of SKUs retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(ref = "ErrorResponse")))
    })
    public ResponseEntity<Page<SKUResponse>> getSKUsByProductId(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @Positive(message = "Product ID must be positive") Long productId,
            @Parameter(description = "Pagination parameters")
            Pageable pageable) {
        return ResponseEntity.ok(skuService.getSKUsByProductId(productId, pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a SKU")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "SKU deleted successfully"),
        @ApiResponse(responseCode = "404", description = "SKU not found",
                    content = @Content(schema = @Schema(ref = "ErrorResponse"))),
        @ApiResponse(responseCode = "409", description = "Cannot delete SKU with active orders",
                    content = @Content(schema = @Schema(ref = "ErrorResponse")))
    })
    public ResponseEntity<Void> deleteSKU(
            @Parameter(description = "SKU ID", required = true)
            @PathVariable @Positive(message = "SKU ID must be positive") Long id) {
        skuService.deleteSKU(id);
        return ResponseEntity.noContent().build();
    }
} 