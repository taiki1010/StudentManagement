package student.management.StudentManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentsCourses;


@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert("INSERT students VALUES(#{id}, #{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{gender}, #{remark}, false)")
  void registerStudent(String id, String name, String kanaName, String nickname, String email,
      String area, int age, String gender, String remark);

  @Insert("INSERT students_courses VALUES(#{id}, #{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  void registerStudentCourse(String id, String studentId, String courseName,
      LocalDateTime courseStartAt, LocalDateTime courseEndAt);
}
