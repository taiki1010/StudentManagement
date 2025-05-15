package student.management.StudentManagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import student.management.StudentManagement.data.ApplicationStatus;
import student.management.StudentManagement.data.Status;
import student.management.StudentManagement.data.Student;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  ObjectMapper mapper;

  @Autowired
  public StudentControllerTest(ObjectMapper mapper) {
    this.mapper = mapper;
  }


  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Student student;
  private StudentCourse studentCourse;
  private ApplicationStatus applicationStatus;
  private StudentCourseWithApplicationStatus studentCourseWithApplicationStatus;
  private List<StudentCourse> studentCourseList;
  private List<StudentCourseWithApplicationStatus> studentCourseWithApplicationStatusList;
  private StudentDetail studentDetail;
  private LocalDateTime now;

  @BeforeEach
  void before() {
    createSampleStudentDetail();
  }

  private void createSampleStudentDetail() {
    now = LocalDateTime.of(2025, 5, 6, 14, 0);
    student = new Student("1", "山田太郎", "ヤマダタロウ", "やまちゃん",
        "yamada@example.com", "東京", 30, "男性", null, false);
    studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
        now.plusMonths(3));
    applicationStatus = new ApplicationStatus("1", "1", Status.TEMPORARY_APPLICATION.getStatus());
    studentCourseWithApplicationStatus = new StudentCourseWithApplicationStatus(studentCourse,
        applicationStatus.getStatus());
    studentCourseWithApplicationStatusList = List.of(studentCourseWithApplicationStatus);
    studentDetail = new StudentDetail(student, studentCourseWithApplicationStatusList);
  }

  @Test
  @DisplayName("getStudentList()の機能実装")
  void 受講生詳細の一覧検索が実行したときにサービスが実行されJsonが返却されること()
      throws Exception {
    List<StudentDetail> studentDetailList = List.of(studentDetail);
    when(service.searchStudentList()).thenReturn(studentDetailList);
    String expectedJson = mapper.writeValueAsString(studentDetailList);

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));

    verify(service, times(1)).searchStudentList();
  }

  @Nested
  class getStudentMethodTest {

    @Test
    @DisplayName("getStudent()の機能実装")
    void 受講生詳細の1人分の検索が実行されたときにサービス実行されJsonが返却されること()
        throws Exception {
      String id = student.getId();
      when(service.searchStudent(id)).thenReturn(studentDetail);
      String expectedJson = mapper.writeValueAsString(studentDetail);

      mockMvc.perform(get("/student/" + id))
          .andExpect(status().isOk())
          .andExpect(content().json(expectedJson));

      verify(service, times(1)).searchStudent(id);
    }

    @Test
    @DisplayName("getStudent()の例外処理")
    void 受講生詳細の検索でidが空のときにNotFoundエラーレスポンスを返すこと() throws Exception {
      mockMvc.perform(get("/student/"))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getStudent()の例外処理")
    void 受講生詳細の検索でidが数字以外のときにBadRequestエラーレスポンスを返すこと()
        throws Exception {
      mockMvc.perform(get("/student/abc"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getStudent()の例外処理")
    void 受講生詳細の検索でidが5桁以上のときにBadRequestエラーレスポンスを返すこと()
        throws Exception {
      mockMvc.perform(get("/student/00000"))
          .andExpect(status().isBadRequest());
    }
  }

  @Test
  @DisplayName("registerStudent()の機能実装")
  void 受講生詳細の登録が実行されたときにOKステータスが返却されること() throws Exception {
    String json = mapper.writeValueAsString(studentDetail);

    mockMvc.perform(post("/registerStudent")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(service, times(1)).insertStudentDetail(studentDetail);
  }

  @Test
  @DisplayName("updateStudent()の機能実装")
  void 受講生詳細の更新が実行されたときにOKステータスが返却されること() throws Exception {
    String json = mapper.writeValueAsString(studentDetail);

    mockMvc.perform(put("/updateStudent")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(studentDetail);
  }

  @Test
  @DisplayName("addStudentCourse()の機能実装")
  void 新しいコース情報の追加がされるときにJson形式のメッセージが返却されること() throws Exception {
    String studentId = "1";
    String json = mapper.writeValueAsString(studentCourse);

    Map<String, String> expectedMessage = new HashMap<String, String>();
    expectedMessage.put("message", "コースが追加されました");
    String expectedJson = mapper.writeValueAsString(expectedMessage);

    mockMvc.perform(post("/students/" + studentId + "/courses")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    verify(service, times(1)).addStudentCourse(studentId, studentCourse);
  }

  @Nested
  class StudentClassValidationTest {

    @Nested
    class idValidationTest {

      @ParameterizedTest
      @ValueSource(strings = {"1", "11", "111", "9999"})
      void 受講生詳細の受講生でIDに4桁以内の数字を用いたときにオブジェクトが作成されること(
          String id) {
        student.setId(id);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(0, violations.size());
      }

      @Test
      void 受講生詳細の受講生でIDに5桁以上の数字を用いたときにバリデーションエラーになること() {
        student.setId("00000");
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(1, violations.size());
        assertEquals("IDは4桁までです", violations.iterator().next().getMessage());
      }

      @Test
      void 受講生詳細の受講生でIDに文字列を用いたときにバリデーションエラーになること() {
        student.setId("abc");
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(1, violations.size());
        assertEquals("IDは数字で入力してください", violations.iterator().next().getMessage());
      }
    }

    @Nested
    class nameValidationTest {

      @ParameterizedTest
      @ValueSource(strings = {"太郎", "Evelyn Starwhisper"})
      void 受講生詳細の受講生で氏名に2文字以上20文字以内の文字列を用いたときにオブジェクトが作成されること(
          String name) {
        student.setName(name);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(0, violations.size());
      }

      @ParameterizedTest
      @ValueSource(strings = {"太", "Seraphina Moonvalentine"})
      void 受講生詳細の受講生で氏名に1文字または20文字以上の文字列を用いたときにバリデーションエラーになること(
          String name) {
        student.setName(name);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(1, violations.size());
        assertEquals("氏名は2文字以上、20文字以内にしてください",
            violations.iterator().next().getMessage());
      }
    }


    @Nested
    class emailValidationTest {

      @ParameterizedTest
      @ValueSource(strings = {"yamada.tarou@gmail.com", "yamada_tarou@yahoo.co.jp"})
      void 受講生詳細の受講生でメールアドレスに適した文字列を用いたときにオブジェクトが作成されること(
          String email) {
        student.setEmail(email);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(0, violations.size());
      }

      @ParameterizedTest
      @ValueSource(strings = {"yamada_tarou", "yamada@.com, @yahoo.co.jp"})
      void 受講生詳細の受講生でメールアドレスに適していない文字列を用いたときにバリデーションエラーになること(
          String email) {
        student.setEmail(email);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(1, violations.size());
        assertEquals("正しいメールアドレスを入力してください",
            violations.iterator().next().getMessage());
      }
    }

    @Nested
    class areaValidationTest {

      @ParameterizedTest
      @ValueSource(strings = {"東京", "京都", "北海道"})
      void 受講生詳細の受講生で地域に適した文字列を用いたときにオブジェクトが作成されること(
          String area) {
        student.setArea(area);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(0, violations.size());
      }

      @ParameterizedTest
      @ValueSource(strings = {"京東", "京都府", "北海道札幌市"})
      void 受講生詳細の受講生で地域に適していない文字列を用いたときにバリデーションエラーになること(
          String area) {
        student.setArea(area);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(1, violations.size());
        assertEquals("都道府県名を正しく入力してください",
            violations.iterator().next().getMessage());
      }
    }

    @Nested
    class genderValidationTest {

      @ParameterizedTest
      @ValueSource(strings = {"男性", "女性", "その他"})
      void 受講生詳細の受講生で性別に適した文字列を用いたときにオブジェクトが作成されること(
          String gender) {
        student.setGender(gender);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(0, violations.size());
      }

      @ParameterizedTest
      @ValueSource(strings = {"男", "女", "トランス"})
      void 受講生詳細の受講生で性別に適していない文字列を用いたときにバリデーションエラーになること(
          String gender) {
        student.setGender(gender);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertEquals(1, violations.size());
        assertEquals("性別は（男性｜女性｜その他）で登録してください",
            violations.iterator().next().getMessage());
      }
    }
  }

  @Nested
  class studentCourseValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"Javaフルコース", "AWSコース", "Webマーケティングコース"})
    void 受講生詳細の受講生コースでコース名に適した文字列を用いたときにオブジェクトが作成されること(
        String name) {
      studentCourse.setCourseName(name);
      Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

      assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Javaコース", "AWS", "Webマーケコース"})
    void 受講生詳細の受講生コースでコース名に適していない文字列を用いたときにバリデーションエラーになること(
        String name) {
      studentCourse.setCourseName(name);
      Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

      assertEquals(1, violations.size());
      assertEquals(
          "コース名は(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)から選択してください",
          violations.iterator().next().getMessage());
    }
  }
}
