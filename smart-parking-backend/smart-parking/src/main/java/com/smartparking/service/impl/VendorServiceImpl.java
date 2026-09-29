package com.smartparking.service.impl;

import com.smartparking.dto.VendorRequest;
import com.smartparking.dto.VendorResponse;
import com.smartparking.entity.Vendor;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.VendorRepository;
import com.smartparking.service.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public VendorResponse create(VendorRequest request) {
        Vendor vendor = new Vendor();
        vendor.setName(request.getName());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhone());
        vendor.setAddress(request.getAddress());
        return toResponse(vendorRepository.save(vendor));
    }

    @Override
    public List<VendorResponse> getAll() {
        return vendorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public VendorResponse getById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id " + id));
        return toResponse(vendor);
    }

    private VendorResponse toResponse(Vendor v) {
        return new VendorResponse(v.getId(), v.getName(), v.getEmail(), v.getPhone(), v.getAddress());
    }
}
