package com.mycompany.exception;

import com.mycompany.dto.ErrorDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Log4j2
public class CustomExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorDTO>> handleBusinessException(BusinessException be){
        for(ErrorDTO errorDTO: be.getErrors()){
            log.error("Business Exception: {} - {}", errorDTO.getCode(), errorDTO.getMsg());
        }
        return new ResponseEntity<>(be.getErrors(), HttpStatus.BAD_REQUEST);
    }
}
