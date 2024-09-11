package com.soulware.backend.global.handler;

import com.soulware.backend.global.exception.NoFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoFileException.class})
    public ResponseEntity<String> noFileExceptionHandler(NoFileException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatusCode.valueOf(452));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
