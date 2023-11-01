package online.interviewpep.Interview.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(InterviewValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(InterviewValidationException ex){
        return genericResponse("failure",12, ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    private ResponseEntity<Map<String, Object>> genericResponse(String status, int statusCode, Object message, HttpStatus HTTP_STATUS){
        Map<String, Object> response = new HashMap<>();

        response.put("status", status);
        response.put("statusCode", statusCode);
        response.put("message", message);

        return ResponseEntity.status(HTTP_STATUS).body(response);

    }
}
