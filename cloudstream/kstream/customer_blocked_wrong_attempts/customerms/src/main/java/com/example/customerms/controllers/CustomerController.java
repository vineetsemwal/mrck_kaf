package com.example.customerms.controllers;

import com.example.customerms.dtos.CustomerResponse;
import com.example.customerms.dtos.LoginRequestDTO;
import com.example.customerms.dtos.RegisterRequestDTO;
import com.example.customerms.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/customers")
@AllArgsConstructor
@RestController
public class CustomerController {

    private ICustomerService customerService;

    @PostMapping
    public CustomerResponse register(@RequestBody RegisterRequestDTO request)throws Throwable{
       return customerService.register(request);
    }

    @PostMapping("/login")
    public CustomerResponse login(@RequestBody LoginRequestDTO request)throws Throwable{
        return customerService.login(request);
    }

    @PutMapping
    public void unblock(@RequestBody String username)throws Throwable{
         customerService.unblock(username);
    }



}
