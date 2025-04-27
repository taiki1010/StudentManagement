package student.management.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  @Transactional
  public void insertStudentDetail(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());

    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      LocalDateTime now = LocalDateTime.now();
      studentsCourse.setCourseStartAt(now);
      studentsCourse.setCourseEndAt(now.plusMonths(3));
      repository.registerStudentCourse(studentsCourse);
    }
  }
}
