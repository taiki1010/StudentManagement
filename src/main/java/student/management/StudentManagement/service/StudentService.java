package student.management.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import student.management.StudentManagement.controller.converter.StudentConverter;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.exception.NotFoundByIdException;
import student.management.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    if (Objects.isNull(student)) {
      throw new NotFoundByIdException("IDに該当する受講生が存在しません");
    }
    List<StudentCourse> studentCourseList = repository.searchStudentCourse(student.getId());
    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日・コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail insertStudentDetail(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    repository.registerStudent(student);
    studentDetail.getStudentCourseList().forEach(studentsCourse -> {
      initStudentsCourse(studentsCourse, student);
      repository.registerStudentCourse(studentsCourse);
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentsCourse 受講生コース情報
   * @param student        受講生
   */
  private void initStudentsCourse(StudentCourse studentsCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentsCourse.setStudentId(student.getId());
    studentsCourse.setCourseStartAt(now);
    studentsCourse.setCourseEndAt(now.plusMonths(3));
  }

  /**
   * 受講生情報の更新を行います。 受講生の情報と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    if (Objects.isNull(repository.searchStudent(studentDetail.getStudent().getId()))) {
      throw new NotFoundByIdException("IDに該当する受講生が存在しません");
    }
    ;
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
  }

}
