package student.management.StudentManagement.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  private List<Student> insertedStudentList;
  private List<StudentCourse> insertedStudentCourseList;

  @BeforeEach
  void before() {
    insertedStudentList = createInsertedStudentList();
    insertedStudentCourseList = createInsertedStudentCourseList();
  }

  private List<Student> createInsertedStudentList() {
    return new ArrayList<Student>(List.of(
        new Student("1", "佐藤 太郎", "サトウ タロウ", "タロちゃん",
            "taro.sato@example.com", "東京", 25, "男性", "", false),
        new Student("2", "鈴木 花子", "スズキ ハナコ", "はなちゃん",
            "hanako.suzuki@example.com", "大阪", 32, "女性", "", false),
        new Student("3", "田中 一郎", "タナカ イチロウ", "イッチー",
            "ichiro.tanaka@example.com", "北海道", 45, "男性", "", false),
        new Student("4", "山本 美咲", "ヤマモト ミサキ", "ミサミサ",
            "misaki.yamamoto@example.com", "福岡", 29, "女性", "", false),
        new Student("5", "中村 颯", "ナカムラ ハヤテ", "ハヤテくん",
            "hayate.nakamura@example.com", "沖縄", 38, "その他", "", false)
    ));
  }

  private List<StudentCourse> createInsertedStudentCourseList() {
    return new ArrayList<StudentCourse>(List.of(
        new StudentCourse("1", "1", "Javaフルコース", LocalDateTime.of(2025, 1, 10, 10, 0),
            LocalDateTime.of(2025, 4, 10, 10, 0)),
        new StudentCourse("2", "2", "AWSコース", LocalDateTime.of(2025, 2, 15, 9, 30),
            LocalDateTime.of(2025, 5, 15, 9, 30)),
        new StudentCourse("3", "3", "デザインコース", LocalDateTime.of(2025, 3, 1, 14, 0),
            LocalDateTime.of(2025, 6, 1, 14, 0)),
        new StudentCourse("4", "4", "WP副業コース", LocalDateTime.of(2025, 4, 5, 13, 0),
            LocalDateTime.of(2025, 7, 5, 13, 0)),
        new StudentCourse("5", "5", "Webマーケティングコース", LocalDateTime.of(2025, 5, 20, 11, 0),
            LocalDateTime.of(2025, 8, 20, 11, 0)),
        new StudentCourse("6", "1", "フロントエンドコース", LocalDateTime.of(2025, 6, 10, 15, 0),
            LocalDateTime.of(2025, 9, 10, 15, 0)),
        new StudentCourse("7", "2", "映像制作コース", LocalDateTime.of(2025, 7, 1, 10, 0),
            LocalDateTime.of(2025, 10, 1, 10, 0)),
        new StudentCourse("8", "3", "英会話コース", LocalDateTime.of(2025, 8, 25, 9, 0),
            LocalDateTime.of(2025, 11, 25, 9, 0)),
        new StudentCourse("9", "4", "Javaフルコース", LocalDateTime.of(2025, 9, 15, 14, 30),
            LocalDateTime.of(2025, 12, 15, 14, 30)),
        new StudentCourse("10", "5", "デザインコース", LocalDateTime.of(2025, 10, 5, 13, 0),
            LocalDateTime.of(2026, 1, 5, 13, 0))
    ));
  }

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> expected = insertedStudentList;
    List<Student> actual = sut.search();

    assertEquals(5, actual.size());
    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
  }

  @Test
  void 受講生の一件検索が行えること() {
    Student expected = new Student("1", "佐藤 太郎", "サトウ タロウ", "タロちゃん",
        "taro.sato@example.com", "東京", 25, "男性", "", false);
    Student actual = sut.searchStudent("1");

    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
  }

  @Test
  void 受講生コースの全件検索が行えること() {
    List<StudentCourse> expected = insertedStudentCourseList;
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertEquals(10, actual.size());
    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));

  }

  @Test
  void 受講生コースの受講生IDに紐づく検索が行えること() {
    List<StudentCourse> expected = List.of(
        new StudentCourse("1", "1", "Javaフルコース", LocalDateTime.of(2025, 1, 10, 10, 0),
            LocalDateTime.of(2025, 4, 10, 10, 0)),
        new StudentCourse("6", "1", "フロントエンドコース", LocalDateTime.of(2025, 6, 10, 15, 0),
            LocalDateTime.of(2025, 9, 10, 15, 0))
    );
    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertEquals(2, actual.size());
    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student(Integer.toString(anyInt()), "山田 太郎", "ヤマダ タロウ",
        "やまちゃん", "yama@example.com", "東京", 30, "男性", "", false
    );

    List<Student> expected = insertedStudentList;
    expected.add(
        new Student("6", "山田 太郎", "ヤマダ タロウ",
            "やまちゃん", "yama@example.com", "東京", 30, "男性", "", false
        )
    );

    sut.registerStudent(student);
    List<Student> actual = sut.search();

    assertEquals(6, actual.size());
    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
    assertEquals("6", actual.get(5).getId()); // 自動インクリメントされているか確認
  }

  @Test
  void 受講生コースの登録が行えること() {
    LocalDateTime now = LocalDateTime.of(2025, 5, 20, 11, 0);
    StudentCourse studentCourse = new StudentCourse(Integer.toString(anyInt()), "1",
        "Webマーケティングコース", now, now.plusMonths(3));

    List<StudentCourse> expected = insertedStudentCourseList;
    expected.add(
        new StudentCourse("11", "1", "Webマーケティングコース", now, now.plusMonths(3))
    );
    sut.registerStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertEquals(11, actual.size());
    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
    assertEquals("11", actual.get(10).getId()); // 自動インクリメントがされているか確認
  }

  @Test
  void 受講生の更新と論理削除が行えること() {
    Student student = new Student("1", "山田 花子", "ヤマダ ハナコ", "はなちゃん",
        "hana@example.com", "大阪", 30, "女性", "更新されました", true
    );

    sut.updateStudent(student);
    Student expected = student;
    Student actual = sut.searchStudent("1");

    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
    assertTrue(actual.isDeleted());
  }

  @Test
  void 受講生コースの更新が行えること() {
    StudentCourse studentCourse = new StudentCourse("1", "1", "AWSコース",
        LocalDateTime.of(2025, 1, 10, 10, 0),
        LocalDateTime.of(2025, 4, 10, 10, 0));

    sut.updateStudentCourse(studentCourse);
    StudentCourse expected = studentCourse;
    StudentCourse actual = sut.searchStudentCourse("1").getFirst();

    assertEquals(expected.hashCode(), actual.hashCode());
    assertTrue(actual.equals(expected));
  }

}
