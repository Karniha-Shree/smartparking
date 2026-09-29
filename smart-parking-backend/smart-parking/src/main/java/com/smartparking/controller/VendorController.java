package com.smartparking.controller;

import com.smartparking.dto.VendorRequest;
import com.smartparking.dto.VendorResponse;
import com.smartparking.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorResponse> create(@Valid @RequestBody VendorRequest request) {
        return new ResponseEntity<>(vendorService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAll() {
        return ResponseEntity.ok(vendorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getById(id));
    }
}
