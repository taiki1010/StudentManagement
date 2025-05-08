package student.management.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;

@Schema(description = "受講生詳細")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Schema(description = "受講生情報")
  @Valid
  private Student student;

  @Schema(description = "受講生コース情報")
  @Valid
  private List<StudentCourse> studentCourseList;
}
