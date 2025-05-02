package student.management.StudentManagement.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import student.management.StudentManagement.exception.response.ErrorResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, String> validatedErrors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> {
          validatedErrors.put(error.getField(), error.getDefaultMessage());
        });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validatedErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseMessage> handleConstraintViolationException(
      ConstraintViolationException ex) {
    String message = ex.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining());
    ErrorResponseMessage response = new ErrorResponseMessage("error occured", message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseMessage> handleNotException(NotFoundException ex) {
    ErrorResponseMessage response = new ErrorResponseMessage("error occured", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }
}
