package student.management.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生リストと受講生コースリストを引数にして実行されると受講生詳細リストが返却されること() {
    Student student = new Student("1", "田中太郎", "タナカタロウ", "タロタロ", "tarou@example.com",
        "東京", 20, "男性", "", false);
    LocalDateTime now = LocalDateTime.now();
    StudentCourse studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
        now.plusMonths(3));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> expected = List.of(new StudentDetail(student, studentCourseList));
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertEquals(expected, actual);
  }
}
