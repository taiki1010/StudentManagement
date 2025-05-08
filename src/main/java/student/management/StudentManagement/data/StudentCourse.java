package student.management.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "受講生コース情報")
@Data
@AllArgsConstructor
public class StudentCourse {

  @Schema(description = "ID、MySQLで自動採番", example = "10")
  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String id;

  @Schema(description = "受講生ID、受講生のデータベースに紐づけ", example = "10")
  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String studentId;

  @Schema(description = "受講コース名", example = "Javaフルコース")
  @Pattern(regexp = "^(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)$",
      message = "コース名は(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)から選択してください")
  private String courseName;

  @Schema(description = "受講開始日", example = "2025-05-02T06:38:53.425Z")
  private LocalDateTime courseStartAt;

  @Schema(description = "受講終了日、開始日から3ヶ月後", example = "2025-08-02T06:38:53.425Z")
  private LocalDateTime courseEndAt;

}
