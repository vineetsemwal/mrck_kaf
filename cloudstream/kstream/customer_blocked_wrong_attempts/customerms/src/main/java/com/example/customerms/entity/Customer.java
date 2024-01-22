package com.example.customerms.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(of="id")
@Data
@Table(name = "customers")
@Entity
public class Customer {
    @GeneratedValue
    @Id
    private Long id;
    private String username;
    private String password;
    private double balance;
    private boolean blocked;//by default false
}
