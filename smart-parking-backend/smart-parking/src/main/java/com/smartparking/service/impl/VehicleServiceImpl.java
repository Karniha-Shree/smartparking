package com.smartparking.service.impl;

import com.smartparking.dto.VehicleRequest;
import com.smartparking.dto.VehicleResponse;
import com.smartparking.entity.Customer;
import com.smartparking.entity.Vehicle;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.CustomerRepository;
import com.smartparking.repository.VehicleRepository;
import com.smartparking.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public VehicleResponse create(VehicleRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setCustomer(customer);

        return toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public VehicleResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = findEntity(id);

        if (!vehicle.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));
            vehicle.setCustomer(customer);
        }

        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());
        return toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public void delete(Long id) {
        vehicleRepository.delete(findEntity(id));
    }

    private Vehicle findEntity(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));
    }

    private VehicleResponse toResponse(Vehicle v) {
        return new VehicleResponse(v.getId(), v.getVehicleNumber(), v.getVehicleType(),
                v.getCustomer().getId(), v.getCustomer().getName());
    }
}
