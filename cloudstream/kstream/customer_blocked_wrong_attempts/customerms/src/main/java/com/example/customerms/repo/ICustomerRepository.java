package com.example.customerms.repo;

import com.example.customerms.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer>findByUsername(String username);

}
