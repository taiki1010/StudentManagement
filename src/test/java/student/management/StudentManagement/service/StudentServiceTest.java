package student.management.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import student.management.StudentManagement.controller.converter.StudentConverter;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.exception.NotFoundException;
import student.management.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  private Student student;
  private StudentCourse studentCourse;
  private List<StudentCourse> studentCourseList;
  private StudentDetail studentDetail;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
    createSampleStudentDetail();
  }

  private void createSampleStudentDetail() {
    LocalDateTime now = LocalDateTime.now();
    student = new Student("1", "山田太郎", "ヤマダタロウ", "やまちゃん",
        "kanou@example.com", "東京", 30, "男性", null, false);
    studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
        now.plusMonths(3));
    studentCourseList = Arrays.asList(studentCourse);
    studentDetail = new StudentDetail(student, studentCourseList);
  }

  @Test
  @DisplayName("searchStudentList()の機能実装")
  void 受講生詳細の一覧検索_リポジトリとコンバータの処理が適切に呼び出せていること() {
    List<Student> studentList = List.of(student);
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  @DisplayName("searchStudent(id)の機能実装")
  void 受講生詳細の一件検索_リポジトリの処理が適切に呼び出せていること() {

    when(repository.searchStudent("1")).thenReturn(student);
    when(repository.searchStudentCourse("1")).thenReturn(studentCourseList);

    StudentDetail expected = studentDetail;
    StudentDetail actual = sut.searchStudent("1");

    verify(repository, times(1)).searchStudent("1");
    verify(repository, times(1)).searchStudentCourse("1");
    assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
  }

  @Test
  @DisplayName("searchStudent(id)の例外処理")
  void 受講生詳細の一件検索_該当するIDの受講生が存在しない場合例外処理が行われること() {
    when(repository.searchStudent("1")).thenReturn(null);

    assertThrows(NotFoundException.class, () -> {
      sut.searchStudent("1");
    });
  }

  @Test
  @DisplayName("InsertStudentDetail(studentDetail)の機能実装")
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること() {

    sut.insertStudentDetail(studentDetail);

    verify(repository, times(1)).registerStudent(studentDetail.getStudent());
    verify(repository, times(1)).registerStudentCourse(
        studentDetail.getStudentCourseList().getFirst());
  }

  @Test
  @DisplayName("updateStudentDetail(studentDetail)の機能実装")
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること() {

    when(repository.searchStudent("1")).thenReturn(student);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

  @Test
  @DisplayName("initStudentsCourse(studentsCourse, student)の機能実装")
  void 受講生詳細の登録_初期化処理が行われること() {
    sut.initStudentsCourse(studentCourse, student);

    assertEquals("1", studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getCourseStartAt().getHour());
    assertEquals(LocalDateTime.now().plusMonths(3).getYear(),
        studentCourse.getCourseEndAt().getYear());
  }

  @Test
  @DisplayName("updateStudentDetail(studentDetail)の例外処理")
  void 受講生更新_該当するIDの受講生が存在しない場合例外処理が行われること() {

    when(repository.searchStudent(studentDetail.getStudent().getId())).thenReturn(null);

    assertThrows(NotFoundException.class, () -> {
      sut.updateStudent(studentDetail);
    });
  }

}
