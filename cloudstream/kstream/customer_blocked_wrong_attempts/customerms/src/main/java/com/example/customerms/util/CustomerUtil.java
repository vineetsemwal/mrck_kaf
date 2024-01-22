package com.example.customerms.util;

import com.example.customerms.dtos.CustomerResponse;
import com.example.customerms.dtos.LoginRequestDTO;
import com.example.customerms.dtos.RegisterRequestDTO;
import com.example.customerms.entity.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerUtil {

    public CustomerResponse toResponse(Customer customer){
        CustomerResponse response=new CustomerResponse();
        BeanUtils.copyProperties(customer,response);
        return  response;
    }

    public Customer toCustomer(LoginRequestDTO request){
        Customer customer=new Customer();
        BeanUtils.copyProperties(request,customer);
        customer.setUsername(customer.getUsername().toLowerCase());
        return customer;
    }

    public Customer toCustomer(RegisterRequestDTO request){
        Customer customer=new Customer();
        BeanUtils.copyProperties(request,customer);
        customer.setUsername(customer.getUsername().toLowerCase());
        return customer;
    }

}
