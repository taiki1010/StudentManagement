package student.management.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import student.management.StudentManagement.controller.converter.StudentConverter;
import student.management.StudentManagement.controller.converter.StudentCourseConverter;
import student.management.StudentManagement.controller.filter.StudentFilter;
import student.management.StudentManagement.data.*;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.exception.NotFoundException;
import student.management.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter studentConverter;
  private StudentCourseConverter studentCourseConverter;
  private StudentFilter studentFilter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter studentConverter,
      StudentCourseConverter studentCourseConverter, StudentFilter studentFilter) {
    this.repository = repository;
    this.studentConverter = studentConverter;
    this.studentCourseConverter = studentCourseConverter;
    this.studentFilter = studentFilter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList(FilterParam filterParam) {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<ApplicationStatus> applicationStatusList = repository.searchApplicationStatusList();

    List<StudentCourseWithApplicationStatus> convertedStudentCourseList = studentCourseConverter.convertStudentCourseWithApplicationStatus(
        studentCourseList, applicationStatusList);
    List<StudentDetail> convertedStudentDetailList = studentConverter.convertStudentDetails(studentList, convertedStudentCourseList);

    if(filterParam.isAllFieldNull()) {
        return convertedStudentDetailList;
    }

    List<StudentDetail> filteredStudentDetailList = studentFilter.filterStudentDetails(convertedStudentDetailList, filterParam);

    if(filteredStudentDetailList.isEmpty()) {
        throw new NotFoundException("条件に該当する受講生は存在しません");
    }

    return filteredStudentDetailList;
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
      throw new NotFoundException("IDに該当する受講生が存在しません");
    }
    List<StudentCourse> studentCourseList = repository.searchStudentCourse(student.getId());
    List<ApplicationStatus> applicationStatusList = repository.searchApplicationStatusList();
    List<StudentCourseWithApplicationStatus> studentCourseWithApplicationStatusList = studentCourseConverter.convertStudentCourseWithApplicationStatus(
        studentCourseList, applicationStatusList);
    return new StudentDetail(student, studentCourseWithApplicationStatusList);
  }

  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日・コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail insertStudentDetail(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    Student student = studentDetail.getStudent();
    String studentId = student.getId();

    studentDetail.getStudentCourseWithApplicationStatusList()
        .forEach(studentCourseWithApplicationStatus -> {
          StudentCourse studentCourse = studentCourseWithApplicationStatus.getStudentCourse();
          String status = Status.TEMPORARY_APPLICATION.getStatus();
          ApplicationStatus applicationStatus = new ApplicationStatus();

          initStudentCourse(studentCourse, studentId);
          repository.registerStudentCourse(studentCourse);

          String studentCourseId = studentCourse.getId();
          applicationStatus.setCourseId(studentCourseId);
          applicationStatus.setStatus(status);
          studentCourseWithApplicationStatus.getApplicationStatus().setStatus(status);
          repository.registerApplicationStatus(applicationStatus);
        });
    return studentDetail;
  }

  /**
   * 受講生情報の更新を行います。 受講生の情報と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    if (Objects.isNull(repository.searchStudent(studentDetail.getStudent().getId()))) {
      throw new NotFoundException("IDに該当する受講生が存在しません");
    }
    ;

    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseWithApplicationStatusList().forEach(
        studentCourseWithApplicationStatus -> {
          StudentCourse studentCourse = studentCourseWithApplicationStatus.getStudentCourse();
          repository.updateStudentCourse(studentCourseWithApplicationStatus.getStudentCourse());

          ApplicationStatus applicationStatus = repository.searchApplicationStatus(
              studentCourse.getId());
          applicationStatus.setStatus(studentCourseWithApplicationStatus.getApplicationStatus().getStatus());
          repository.updateApplicationStatus(applicationStatus);
        }
    );
  }

  /**
   * 受講生コースを新しく追加します。
   *
   * @param studentId     　受講生ID
   * @param studentCourse 受講生コース
   */
  @Transactional
  public void addStudentCourse(String studentId, StudentCourse studentCourse) {
    initStudentCourse(studentCourse, studentId);
    repository.registerStudentCourse(studentCourse);

    String status = Status.TEMPORARY_APPLICATION.getStatus();
    String studentCourseId = studentCourse.getId();
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setCourseId(studentCourseId);
    applicationStatus.setStatus(status);
    repository.registerApplicationStatus(applicationStatus);
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse 受講生コース情報
   * @param studentId     受講生
   */
  void initStudentCourse(StudentCourse studentCourse, String studentId) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(studentId);
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusMonths(3));
  }

}
