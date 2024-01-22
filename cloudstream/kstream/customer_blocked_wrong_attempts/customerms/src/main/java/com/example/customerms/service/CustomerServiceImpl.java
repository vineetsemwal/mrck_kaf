package com.example.customerms.service;

import com.example.customerms.dtos.CustomerResponse;
import com.example.customerms.dtos.LoginRequestDTO;
import com.example.customerms.dtos.RegisterRequestDTO;
import com.example.customerms.entity.Customer;
import com.example.customerms.exceptions.BlockedException;
import com.example.customerms.exceptions.CustomerNotFoundException;
import com.example.customerms.exceptions.IncorrectCredentialsException;
import com.example.customerms.repo.ICustomerRepository;
import com.example.customerms.util.CustomerUtil;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@AllArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService {

    private ICustomerRepository customerRepo;
    private CustomerUtil customerUtil;
    private StreamBridge streamBridge;

    @Transactional
    @Override
    public CustomerResponse register(final RegisterRequestDTO request) {
        Customer customer = customerUtil.toCustomer(request);
        customer.setUsername(customer.getUsername().toLowerCase());
        customer = customerRepo.save(customer);
        return customerUtil.toResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse login(final LoginRequestDTO request) throws IncorrectCredentialsException {
        Optional<Customer> optional = customerRepo.findByUsername(request.getUsername().toLowerCase());
        Customer customer = optional.orElseThrow(() -> new IncorrectCredentialsException("incorrect credentials")
        );
        if(customer.isBlocked()){
           throw new BlockedException(request.getUsername()+":customer is blocked");
        }
        if (!customer.getPassword().equals(request.getPassword())) {

            Message<String> message= MessageBuilder.withPayload(request.getUsername())
                    .setHeader(KafkaHeaders.KEY,request.getUsername().getBytes())
                    .build();

           streamBridge.send("wrongPasswords-out-0", message);
            throw new IncorrectCredentialsException("incorrect credentials");
        }
        return customerUtil.toResponse(customer);
    }


    @Transactional
    @Override
    public void block(final String username) throws CustomerNotFoundException{
        System.out.println("****locking account");
       Optional<Customer>optional= customerRepo.findByUsername(username.toLowerCase());
       Customer customer=optional.orElseThrow(()->new CustomerNotFoundException("customer not found:"+username));
       customer.setBlocked(true);
       customerRepo.save(customer);

    }

    @Transactional
    @Override
    public void unblock(String username) throws CustomerNotFoundException {
        Optional<Customer>optional= customerRepo.findByUsername(username.toLowerCase());
        Customer customer=optional.orElseThrow(()->new CustomerNotFoundException("customer not found:"+username));
        customer.setBlocked(false);
        customerRepo.save(customer);

    }
}
