package com.paypal.bfs.test.employeeserv.exception.handler;

import com.paypal.bfs.test.employeeserv.api.model.ResponseParameter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class EmployeeExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseParameter> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    log.error("constraint violation", exception);
    ResponseParameter parameter = new ResponseParameter();
    parameter.setTimestamp(LocalDateTime.now().toString());
    parameter.setReasonCode("ERR001");
    List<String> errors = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
      if (objectError instanceof FieldError) {
        FieldError fe = (FieldError) objectError;
        return "Path : " + fe.getField() + ", Message: " + fe.getDefaultMessage();
      } else {
        return objectError.toString();
      }
    }).collect(Collectors.toList());
    parameter.setAdditionalInfo(errors);

    return new ResponseEntity<>(parameter, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseParameter> handleConstraintViolationException(ConstraintViolationException exception) {
    log.error("constraint violation", exception);
    ResponseParameter parameter = new ResponseParameter();
    parameter.setTimestamp(LocalDateTime.now().toString());
    parameter.setReasonCode("ERR001");
    List<String> errors = exception.getConstraintViolations().stream().map(constraint -> "Path : " + constraint.getPropertyPath() + ", Message : " + constraint.getMessage())
        .collect(Collectors.toList());
    parameter.setAdditionalInfo(errors);

    return new ResponseEntity<>(parameter, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseParameter> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
    log.error("constraint violation", exception);
    ResponseParameter parameter = new ResponseParameter();
    parameter.setTimestamp(LocalDateTime.now().toString());
    parameter.setReasonCode("ERR001");
    parameter.setAdditionalInfo(Arrays.asList(exception.getMostSpecificCause().getMessage()));

    return new ResponseEntity<>(parameter, HttpStatus.BAD_REQUEST);
  }

}
