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
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    StudentsCourses studentsCourses = studentDetail.getStudentsCourses().getFirst();
    studentsCourses.setStudentId(student.getId());
    studentsCourses.setCourseStartAt(LocalDateTime.now());
    studentsCourses.setCourseEndAt(LocalDateTime.now().plusMonths(3));
    repository.registerStudentCourse(studentsCourses);
  }

}
