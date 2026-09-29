package com.smartparking.service.impl;

import com.smartparking.dto.CustomerRequest;
import com.smartparking.dto.CustomerResponse;
import com.smartparking.entity.Customer;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.CustomerRepository;
import com.smartparking.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    // Constructor injection (preferred over @Autowired field injection).
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse getById(Long id) {
        Customer customer = findEntity(id);
        return toResponse(customer);
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = findEntity(id);
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return toResponse(customerRepository.save(customer));
    }

    @Override
    public void delete(Long id) {
        Customer customer = findEntity(id);
        customerRepository.delete(customer);
    }

    private Customer findEntity(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress());
    }
}
