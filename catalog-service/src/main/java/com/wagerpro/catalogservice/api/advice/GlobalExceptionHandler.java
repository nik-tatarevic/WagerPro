package com.wagerpro.catalogservice.api.advice;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wagerpro.catalogservice.domain.exception.InvalidMatchStatusException;
import com.wagerpro.catalogservice.domain.exception.InvalidOddValueException;
import com.wagerpro.catalogservice.domain.exception.LeagueNotFoundException;
import com.wagerpro.catalogservice.domain.exception.MatchNotFoundException;
import com.wagerpro.catalogservice.domain.exception.OddNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(MatchNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Match not found", exception.getMessage());
    }
    
    @ExceptionHandler(LeagueNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(LeagueNotFoundException exception) {
    	return problem(HttpStatus.NOT_FOUND, "League not found", exception.getMessage());
    }
    
    @ExceptionHandler(OddNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(OddNotFoundException exception) {
    	return problem(HttpStatus.NOT_FOUND, "Odd not found", exception.getMessage());
    }

    @ExceptionHandler(InvalidOddValueException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCredentials(InvalidOddValueException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid odd data", exception.getMessage());
    }
    
    @ExceptionHandler(InvalidMatchStatusException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCredentials(InvalidMatchStatusException exception) {
    	return problem(HttpStatus.BAD_REQUEST, "Invalid match status data", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .toList();
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "One or more request fields are invalid.");
        body.setTitle("Request validation failed");
        body.setProperty("timestamp", Instant.now());
        body.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleUnreadableBody(HttpMessageNotReadableException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Malformed request", "The request body is missing or malformed.");
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception exception) {
        log.error("Unexpected API error", exception);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error",
                "An unexpected error occurred.");
    }

    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle(title);
        body.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(status).body(body);
    }
    
}
