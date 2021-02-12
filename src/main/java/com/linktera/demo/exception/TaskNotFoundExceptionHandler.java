package com.linktera.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class TaskNotFoundExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("Task not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
