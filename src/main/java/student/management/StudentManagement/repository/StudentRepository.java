package student.management.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentsCourses;


@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert(
      "INSERT students (name, nickname, kana_name, email, area, age, gender, remark, is_deleted) "
          + "VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT students_courses (student_id, course_name, course_start_at, course_end_at) "
      + "VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourse(StudentsCourses studentsCourse);
}
