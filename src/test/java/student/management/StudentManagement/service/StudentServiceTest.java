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
import student.management.StudentManagement.controller.converter.StudentCourseConverter;
import student.management.StudentManagement.data.ApplicationStatus;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.exception.NotFoundException;
import student.management.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter studentConverter;

  @Mock
  private StudentCourseConverter studentCourseConverter;

  private StudentService sut;

  private Student student;
  private StudentCourse studentCourse;
  private ApplicationStatus applicationStatus;
  private StudentCourseWithApplicationStatus studentCourseWithApplicationStatus;
  private List<StudentCourse> studentCourseList;
  private List<ApplicationStatus> applicationStatusList;
  private List<StudentCourseWithApplicationStatus> studentCourseWithApplicationStatusList;
  private StudentDetail studentDetail;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, studentConverter, studentCourseConverter);
    createSampleStudentDetail();
  }

  private void createSampleStudentDetail() {
    LocalDateTime now = LocalDateTime.now();
    student = new Student("1", "山田太郎", "ヤマダタロウ", "やまちゃん",
        "kanou@example.com", "東京", 30, "男性", null, false);
    studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
        now.plusMonths(3));
    applicationStatus = new ApplicationStatus("1", "1", "仮申込");

    studentCourseWithApplicationStatus = new StudentCourseWithApplicationStatus(studentCourse,
        applicationStatus.getStatus());
    studentCourseWithApplicationStatusList = Arrays.asList(studentCourseWithApplicationStatus);
    studentDetail = new StudentDetail(student, studentCourseWithApplicationStatusList);
  }

  @Test
  @DisplayName("searchStudentList()の機能実装")
  void 受講生詳細の一覧検索_リポジトリとコンバータの処理が適切に呼び出せていること() {
    List<Student> studentList = List.of(student);
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(repository.searchApplicationStatusList()).thenReturn(applicationStatusList);
    when(studentCourseConverter.convertStudentCourseWithApplicationStatus(studentCourseList,
        applicationStatusList)).thenReturn(studentCourseWithApplicationStatusList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(studentConverter, times(1)).convertStudentDetails(studentList,
        studentCourseWithApplicationStatusList);
  }

  @Test
  @DisplayName("searchStudent(id)の機能実装")
  void 受講生詳細の一件検索_リポジトリの処理が適切に呼び出せていること() {

    when(repository.searchStudent("1")).thenReturn(student);
    when(repository.searchStudentCourse("1")).thenReturn(studentCourseList);
    when(repository.searchApplicationStatusList()).thenReturn(applicationStatusList);
    when(studentCourseConverter.convertStudentCourseWithApplicationStatus(studentCourseList,
        applicationStatusList))
        .thenReturn(studentCourseWithApplicationStatusList);

    StudentDetail expected = studentDetail;
    StudentDetail actual = sut.searchStudent("1");

    verify(repository, times(1)).searchStudent("1");
    verify(repository, times(1)).searchStudentCourse("1");
    verify(studentCourseConverter, times(1)).convertStudentCourseWithApplicationStatus(
        studentCourseList, applicationStatusList);
    assertEquals(expected, actual);
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
    ApplicationStatus expectedApplicationStatus = new ApplicationStatus(null, "1", "仮申込");

    sut.insertStudentDetail(studentDetail);

    verify(repository, times(1)).registerStudent(studentDetail.getStudent());
    verify(repository, times(1)).registerStudentCourse(
        studentDetail.getStudentCourseWithApplicationStatusList().getFirst().getStudentCourse());
    verify(repository, times(1)).registerApplicationStatus(expectedApplicationStatus);
  }

  @Test
  @DisplayName("initStudentsCourse(studentsCourse, student)の機能実装")
  void 受講生詳細の登録_初期化処理が行われること() {
    sut.initStudentCourse(studentCourse, student.getId());

    assertEquals("1", studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getCourseStartAt().getHour());
    assertEquals(LocalDateTime.now().plusMonths(3).getYear(),
        studentCourse.getCourseEndAt().getYear());
  }


  @Test
  @DisplayName("updateStudentDetail(studentDetail)の機能実装")
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること() {

    when(repository.searchStudent("1")).thenReturn(student);
    when(repository.searchApplicationStatus("1")).thenReturn(
        applicationStatus);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(studentDetail.getStudent());
    verify(repository, times(1)).updateStudentCourse(studentCourse);
    verify(repository, times(1)).updateApplicationStatus(applicationStatus);
  }

  @Test
  @DisplayName("updateStudentDetail(studentDetail)の例外処理")
  void 受講生更新_該当するIDの受講生が存在しない場合例外処理が行われること() {

    when(repository.searchStudent(studentDetail.getStudent().getId())).thenReturn(null);

    assertThrows(NotFoundException.class, () -> {
      sut.updateStudent(studentDetail);
    });
  }

  @Test
  @DisplayName("addStudentCourse(studentId, studentCourseの機能実装)")
  void コース追加_リポジトリの処理が適切に呼び出せていること() {
    String studentId = "1";
    ApplicationStatus expectedApplicationStatus = new ApplicationStatus(null, "1", "仮申込");

    sut.addStudentCourse(studentId, studentCourse);

    verify(repository, times(1)).registerStudentCourse(studentCourse);
    verify(repository, times(1)).registerApplicationStatus(expectedApplicationStatus);
  }

}
