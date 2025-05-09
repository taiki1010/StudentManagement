package student.management.StudentManagement.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertEquals(5, actual.size());
  }

  @Test
  void 受講生の一件検索が行えること() {
    Student actual = sut.searchStudent("1");

    assertEquals("佐藤 太郎", actual.getName());
    assertEquals("サトウ タロウ", actual.getKanaName());
    assertEquals("タロちゃん", actual.getNickname());
    assertEquals("taro.sato@example.com", actual.getEmail());
    assertEquals("東京", actual.getArea());
    assertEquals(25, actual.getAge());
    assertEquals("男性", actual.getGender());
    assertEquals("", actual.getRemark());
    assertFalse(actual.isDeleted());
  }

  @Test
  void 受講生コースの全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertEquals(10, actual.size());
  }

  @Test
  void 受講生コースの受講生IDに紐づく検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertEquals(2, actual.size());

    assertEquals("1", actual.get(0).getId());
    assertEquals("Javaフルコース", actual.get(0).getCourseName());
    assertEquals("2025-01-10 10:00:00", actual.get(0).getCourseStartAt().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    assertEquals("2025-04-10 10:00:00", actual.get(0).getCourseEndAt().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    assertEquals("6", actual.get(1).getId());
    assertEquals("フロントエンドコース", actual.get(1).getCourseName());
    assertEquals("2025-06-10 15:00:00", actual.get(1).getCourseStartAt().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    assertEquals("2025-09-10 15:00:00", actual.get(1).getCourseEndAt().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("山田 太郎");
    student.setKanaName("ヤマダ タロウ");
    student.setNickname("やまちゃん");
    student.setEmail("yama@example.com");
    student.setArea("東京");
    student.setAge(30);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();
    assertEquals(6, actual.size());
    assertEquals("6", actual.get(5).getId()); //インクリメントされているか確認
  }

  @Test
  void 受講生コースの登録が行えること() {
    LocalDateTime now = LocalDateTime.now();
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Javaフルコース");
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusMonths(3));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertEquals(11, actual.size());
    assertEquals("11", actual.get(10).getId()); // インクリメントがされているか確認
  }

  @Test
  void 受講生の更新と論理削除が行えること() {
    Student student = new Student();
    student.setId("1");
    student.setName("山田 花子");
    student.setKanaName("ヤマダ ハナコ");
    student.setNickname("はなちゃん");
    student.setEmail("hana@example.com");
    student.setArea("大阪");
    student.setAge(30);
    student.setGender("女性");
    student.setRemark("更新されました");
    student.setDeleted(true);

    sut.updateStudent(student);

    Student actual = sut.searchStudent("1");

    assertEquals("山田 花子", actual.getName());
    assertEquals("ヤマダ ハナコ", actual.getKanaName());
    assertEquals("はなちゃん", actual.getNickname());
    assertEquals("hana@example.com", actual.getEmail());
    assertEquals("大阪", actual.getArea());
    assertEquals(30, actual.getAge());
    assertEquals("女性", actual.getGender());
    assertEquals("更新されました", actual.getRemark());
    assertTrue(actual.isDeleted());
  }

  @Test
  void 受講生コースの更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setCourseName("AWSコース");

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertEquals("AWSコース", actual.getFirst().getCourseName());
  }

}
