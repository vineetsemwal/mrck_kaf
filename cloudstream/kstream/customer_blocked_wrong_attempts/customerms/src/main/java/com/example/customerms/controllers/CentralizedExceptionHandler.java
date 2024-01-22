package com.example.customerms.controllers;

import com.example.customerms.exceptions.BlockedException;
import com.example.customerms.exceptions.CustomerNotFoundException;
import com.example.customerms.exceptions.IncorrectCredentialsException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CentralizedExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({IncorrectCredentialsException.class, BlockedException.class})
    public String handleIncorrectCredentials(Exception e){
      log.debug("exception handled incorrect cred",e);
      return e.getMessage();
    }




    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CustomerNotFoundException.class,ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public String handleConstraintViolation(Exception e){
        log.debug("constraint violation handled",e);
        return e.getMessage();
    }

}
