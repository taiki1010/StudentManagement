package student.management.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import student.management.StudentManagement.controller.converter.StudentConverter;
import student.management.StudentManagement.data.StudentCourse;
import student.management.StudentManagement.domain.StudentDetail;
import student.management.StudentManagement.exception.NotFoundException;
import student.management.StudentManagement.exception.response.ErrorResponseMessage;
import student.management.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @Operation(summary = "受講生詳細の一覧検索", description = "受講生詳細を一覧検索します。ただし、論理削除されている方は該当しません(is_deleted = true)",
      responses = {@ApiResponse(
          responseCode = "200", description = "ok",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class)
              ))
      )}
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() throws NotFoundException {
    return service.searchStudentList();
  }

  @GetMapping("/filterStudentList")
  public List<StudentDetail> filterStudentList() {
      return service.filterStudentDetailList();
  }

  @Operation(
      summary = "受講生検索", description = "受講生一人分の詳細を検索します。idに紐づく生徒が存在しない場合エラーが発生します。",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "404", description = "指定されたidが存在しない場合のエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseMessage.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "idが数字以外で検索された場合",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseMessage.class)
              )
          )
      },
      parameters = {
          @Parameter(in = ParameterIn.PATH, name = "id",
              required = true,
              description = "受講生ID",
              schema = @Schema(
                  type = "String",
                  description = "AUTO_INCREMENTで自動採番されたid番号",
                  example = "10"
              ))
      }
  )
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable @NotBlank(message = "検索するためのIDが空になっています")
      @Pattern(regexp = "^\\d+$", message = "IDは数字を指定してください")
      @Size(min = 1, max = 4, message = "検索IDは4桁以内にしてください")
      String id) {
    return service.searchStudent(id);
  }

  @Operation(summary = "受講生登録", description = "受講生1人分の情報を登録します。リクエストはjson形式",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "リクエストボディのバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseMessage.class)
              )
          )
      }
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "登録する受講生詳細",
      required = true,
      content = @Content(schema = @Schema(implementation = StudentDetail.class))
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.insertStudentDetail(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  @Operation(summary = "受講生更新", description = "受講生1人分の情報を更新します。リクエストはjson形式",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "リクエストボディのバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseMessage.class)
              )
          )
      }
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "更新する受講生詳細",
      required = true,
      content = @Content(schema = @Schema(implementation = StudentDetail.class))
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました");
  }

  @PostMapping("/students/{studentId}/courses")
  public ResponseEntity<Map<String, String>> addStudentCourse(
      @RequestBody @Valid StudentCourse studentCourse,
      @PathVariable("studentId") @NotBlank(message = "IDが空になっています")
      @Pattern(regexp = "^\\d+$", message = "IDは数字を指定してください")
      @Size(min = 1, max = 4, message = "IDは4桁以内にしてください") String studentId) {
    service.addStudentCourse(studentId, studentCourse);
    Map<String, String> message = new HashMap<String, String>();
    message.put("message", "コースが追加されました");
    return ResponseEntity.ok(message);
  }
}
