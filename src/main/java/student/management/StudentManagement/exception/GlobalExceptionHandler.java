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

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map> handleException(MethodArgumentNotValidException ex) {
    Map<String, String> validatedErrors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> {
          validatedErrors.put(error.getField(), error.getDefaultMessage());
        });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validatedErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map> handleException(ConstraintViolationException ex) {
    String message = ex.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining());
    Map<String, String> validatedError = new HashMap<String, String>() {
      {
        put("error", message);
      }
    };
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validatedError);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map> handleException(NotFoundException ex) {
    Map<String, String> validatedError = new HashMap<String, String>() {
      {
        put("error", ex.getMessage());
      }
    };
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validatedError);
  }

}
