package com.example.customerms.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of="username")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
