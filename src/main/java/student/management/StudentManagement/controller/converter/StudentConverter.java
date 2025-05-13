package student.management.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;
import student.management.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @param studentList       受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourseWithApplicationStatus> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourseWithApplicationStatus> convertStudentCourseListWithApplicationStatus = studentCourseList.stream()
          .filter(studentCourse -> student.getId()
              .equals(studentCourse.getStudentCourse().getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseWithApplicationStatusList(
          convertStudentCourseListWithApplicationStatus);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}
