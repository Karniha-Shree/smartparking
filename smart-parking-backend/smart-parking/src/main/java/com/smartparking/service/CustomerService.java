package com.smartparking.service;

import com.smartparking.dto.CustomerRequest;
import com.smartparking.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    List<CustomerResponse> getAll();
    CustomerResponse getById(Long id);
    CustomerResponse update(Long id, CustomerRequest request);
    void delete(Long id);
}
