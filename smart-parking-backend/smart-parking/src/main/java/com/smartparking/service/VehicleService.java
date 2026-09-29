package com.smartparking.service;

import com.smartparking.dto.VehicleRequest;
import com.smartparking.dto.VehicleResponse;

import java.util.List;

public interface VehicleService {
    VehicleResponse create(VehicleRequest request);
    List<VehicleResponse> getAll();
    VehicleResponse getById(Long id);
    VehicleResponse update(Long id, VehicleRequest request);
    void delete(Long id);
}
