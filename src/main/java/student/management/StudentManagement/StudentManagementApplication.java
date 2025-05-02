package student.management.StudentManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "受講生管理システム", description = "受講生を管理するためのシステムです。"))
@SpringBootApplication
public class StudentManagementApplication {


  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }
}
