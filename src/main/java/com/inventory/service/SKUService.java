package com.inventory.service;

import com.inventory.dto.request.SKURequest;
import com.inventory.dto.response.SKUResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SKUService {
    SKUResponse createSKU(SKURequest request);
    SKUResponse updateSKU(Long id, SKURequest request);
    SKUResponse getSKUById(Long id);
    Page<SKUResponse> getSKUsByProductId(Long productId, Pageable pageable);
    void deleteSKU(Long id);
} 