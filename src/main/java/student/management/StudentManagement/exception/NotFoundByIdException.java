package student.management.StudentManagement.exception;

public class NotFoundByIdException extends RuntimeException {

  public NotFoundByIdException() {
    super();
  }

  public NotFoundByIdException(String message) {
    super(message);
  }

  public NotFoundByIdException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundByIdException(Throwable cause) {
    super(cause);
  }
}
