package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private String phone;

    private String address;
}
