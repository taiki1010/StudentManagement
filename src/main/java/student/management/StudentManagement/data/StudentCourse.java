package student.management.StudentManagement.data;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  @Pattern(regexp = "^\\d+$")
  private String id;
  @Pattern(regexp = "^\\d+$")
  private String studentId;
  private String courseName;
  private LocalDateTime courseStartAt;
  private LocalDateTime courseEndAt;
}
