package com.example.customerms.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "username")
@Data
public class RegisterRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Min(0)
    private double balance;
}
