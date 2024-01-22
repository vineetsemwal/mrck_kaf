package com.example.customerms.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CustomerResponse {
    private Long id;
    private String username;
    private String password;
    private double balance;

}
