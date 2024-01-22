package com.example.customerms.service;

import com.example.customerms.dtos.CustomerResponse;
import com.example.customerms.dtos.LoginRequestDTO;
import com.example.customerms.dtos.RegisterRequestDTO;
import com.example.customerms.exceptions.CustomerNotFoundException;
import com.example.customerms.exceptions.IncorrectCredentialsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ICustomerService {
    CustomerResponse register(@Valid @NotNull RegisterRequestDTO request);
    CustomerResponse login(@Valid @NotNull LoginRequestDTO request ) throws IncorrectCredentialsException;

    void block(@Length(min = 2) String username) throws CustomerNotFoundException;

    void unblock(@Length(min = 2)String username) throws CustomerNotFoundException;
}
