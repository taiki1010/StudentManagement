package student.management.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentsCourses;
import student.management.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {

    //年齢が30代の人のみを抽出する
    //抽出したリストをコントローラーに返す。
    List<Student> searchedStudentList = repository.search();

    return searchedStudentList.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .toList();
  }

  public List<StudentsCourses> searchStudentsCourseList() {

    //「Javaフルコース」のコース情報のみを抽出する
    //抽出したリストをコントローラーに返す。
    List<StudentsCourses> searchedStudentsCourseList = repository.searchStudentsCourses();

    return searchedStudentsCourseList.stream()
        .filter(studentsCourse -> studentsCourse.getCourseName().equals("Javaフルコース"))
        .toList();
  }

}
