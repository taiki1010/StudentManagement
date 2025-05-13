package student.management.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import student.management.StudentManagement.data.ApplicationStatus;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;

@Component
public class StudentCourseConverter {

  public List<StudentCourseWithApplicationStatus> convertStudentCourseWithApplicationStatus(
      List<StudentCourse> studentCourseList, List<ApplicationStatus> applicationStatusList) {
    List<StudentCourseWithApplicationStatus> studentCourseWithApplicationStatusList = new ArrayList<>();
    studentCourseList.forEach(studentCourse -> {
      StudentCourseWithApplicationStatus studentCourseWithApplicationStatus = new StudentCourseWithApplicationStatus();
      applicationStatusList.forEach(applicationStatus -> {
        if (Objects.equals(studentCourse.getId(), applicationStatus.getCourseId())) {
          studentCourseWithApplicationStatus.setStudentCourse(studentCourse);
          studentCourseWithApplicationStatus.setStatus(applicationStatus.getStatus());
        }
      });
      studentCourseWithApplicationStatusList.add(studentCourseWithApplicationStatus);
    });
    return studentCourseWithApplicationStatusList;
  }
}
