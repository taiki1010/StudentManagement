package student.management.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatus {

  @Schema(description = "ID、MySQLで自動採番", example = "10")
  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String id;

  @Schema(description = "受講生ID、受講生のデータベースに紐づけ", example = "10")
  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String courseId;

  @Schema(description = "受講コース名", example = "Javaフルコース")
  @Pattern(regexp = "^(仮申込|本申込|受講中|受講修了)$",
      message = "コース名は(仮申込|本申込|受講中|受講修了)から選択してください")
  private String status;
}
