package student.management.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import student.management.StudentManagement.data.ApplicationStatus;
import student.management.StudentManagement.data.Status;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;
import student.management.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  @Test
  void 受講生リストと受講生コースリストを引数にして実行されると受講生詳細リストが返却されること() {
    StudentConverter sut = new StudentConverter();
    Student student = new Student("1", "田中太郎", "タナカタロウ", "タロタロ", "tarou@example.com",
        "東京", 20, "男性", "", false);
    LocalDateTime now = LocalDateTime.now();
    StudentCourse studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
        now.plusMonths(3));
    ApplicationStatus applicationStatus = new ApplicationStatus("1", "1",
        Status.TemporaryApplication.getStatus());
    StudentCourseWithApplicationStatus studentCourseWithApplicationStatus = new StudentCourseWithApplicationStatus(
        studentCourse, applicationStatus.getStatus());

    List<Student> studentList = List.of(student);
    List<StudentCourseWithApplicationStatus> studentCourseWithApplicationStatusList = List.of(
        studentCourseWithApplicationStatus);

    List<StudentDetail> expected = List.of(
        new StudentDetail(student, studentCourseWithApplicationStatusList));
    List<StudentDetail> actual = sut.convertStudentDetails(studentList,
        studentCourseWithApplicationStatusList);

    assertEquals(expected, actual);
  }
}
