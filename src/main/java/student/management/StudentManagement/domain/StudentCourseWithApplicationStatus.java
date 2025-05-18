package student.management.StudentManagement.domain;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import student.management.StudentManagement.data.ApplicationStatus;
import student.management.StudentManagement.data.StudentCourse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseWithApplicationStatus {

  @Valid
  private StudentCourse studentCourse;

  @Valid
  private ApplicationStatus applicationStatus;
}
