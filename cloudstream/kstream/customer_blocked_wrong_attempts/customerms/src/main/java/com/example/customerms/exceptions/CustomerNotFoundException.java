package com.example.customerms.exceptions;

import com.example.customerms.dtos.CustomerResponse;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String msg){
        super(msg);
    }



}
