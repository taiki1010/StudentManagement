package student.management.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentsCourses;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {

    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {

    return repository.searchStudentsCourses();
  }

  public void insertStudentDetail(StudentDetail studentDetail) {
//    List<Student> existStudents = repository.search();
    String studentId = String.valueOf(repository.search().size() + 1);
    String studentName = studentDetail.getStudent().getName();
    String kanaName = studentDetail.getStudent().getKanaName();
    String nickname = studentDetail.getStudent().getKanaName();
    String email = studentDetail.getStudent().getEmail();
    String area = studentDetail.getStudent().getArea();
    int age = studentDetail.getStudent().getAge();
    String gender = studentDetail.getStudent().getGender();
    String remark = studentDetail.getStudent().getRemark();
    repository.registerStudent(studentId, studentName, kanaName, nickname, email, area, age, gender,
        remark);

    String courseId = String.valueOf(repository.searchStudentsCourses().size() + 1);
    String courseName = studentDetail.getStudentsCourses().getFirst().getCourseName();
    LocalDateTime courseStartAt = LocalDateTime.now();
    LocalDateTime courseEndAt = courseStartAt.plusMonths(3);
    repository.registerStudentCourse(courseId, studentId, courseName, courseStartAt, courseEndAt);
  }

}
