package student.management.StudentManagement.data;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String id;

  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String studentId;

  @Pattern(regexp = "^(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)$",
      message = "コース名は(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)から選択してください")
  private String courseName;

  private LocalDateTime courseStartAt;

  private LocalDateTime courseEndAt;
}
