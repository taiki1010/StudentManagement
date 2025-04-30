package student.management.StudentManagement.exception;

import java.util.HashMap;
import java.util.Map;
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

  @ExceptionHandler(NotFoundByIdException.class)
  public ResponseEntity<String> handleException(NotFoundByIdException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
  }
  
}
