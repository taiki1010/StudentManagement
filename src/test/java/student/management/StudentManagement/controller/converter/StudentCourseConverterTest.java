package student.management.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import student.management.StudentManagement.data.ApplicationStatus;
import student.management.StudentManagement.data.Status;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;

class StudentCourseConverterTest {

  @Test
  void 受講生コースと申込情報を渡すと申込情報が付与された受講生コース情報が帰ること() {

    StudentCourseConverter sut = new StudentCourseConverter();

    LocalDateTime now = LocalDateTime.now();
    StudentCourse studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
        now.plusMonths(3));
    ApplicationStatus applicationStatus = new ApplicationStatus("1", "1",
        Status.TEMPORARY_APPLICATION.getStatus());
    StudentCourseWithApplicationStatus studentCourseWithApplicationStatus = new StudentCourseWithApplicationStatus(
        studentCourse, applicationStatus.getStatus());

    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentCourseWithApplicationStatus> expected = List.of(studentCourseWithApplicationStatus);
    List<StudentCourseWithApplicationStatus> actual = sut.convertStudentCourseWithApplicationStatus(
        studentCourseList, applicationStatusList);

    assertEquals(expected, actual);
  }

}
