package com.smartparking.service;

import com.smartparking.dto.VendorRequest;
import com.smartparking.dto.VendorResponse;

import java.util.List;

public interface VendorService {
    VendorResponse create(VendorRequest request);
    List<VendorResponse> getAll();
    VendorResponse getById(Long id);
}
