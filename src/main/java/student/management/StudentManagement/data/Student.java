package student.management.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生情報")
@Getter
@Setter
@AllArgsConstructor
public class Student {

  @Schema(description = "ID、MySQLで自動採番", example = "10")
  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String id;

  @Schema(description = "氏名", example = "田中太郎")
  @Size(min = 2, max = 20, message = "名前は20文字以内にしてください")
  private String name;

  @Schema(description = "カナ名", example = "タナカタロウ")
  @Size(min = 2, max = 20, message = "カナ名は20文字以内にしてください")
  private String kanaName;

  @Schema(description = "ニックネーム", example = "たろちゃん")
  @Size(min = 2, max = 20, message = "ニックネームは20文字以内にしてください")
  private String nickname;

  @Schema(description = "メールアドレス", example = "tanaka@gmail.com")
  @Email(message = "正しいメールアドレスを入力してください")
  private String email;

  @Schema(description = "都道府県名（都府県の表示は記載しない、北海道は除く）", example = "東京　京都 北海道など")
  @Pattern(regexp = "^(北海道|青森|岩手|宮城|秋田|山形|福島|茨城|栃木|群馬|埼玉|千葉|東京|神奈川|新潟|富山|石川|福井|山梨|長野|岐阜|静岡|愛知|三重|滋賀|京都|大阪|兵庫|奈良|和歌山|鳥取|島根|岡山|広島|山口|徳島|香川|愛媛|高知|福岡|佐賀|長崎|熊本|大分|宮崎|鹿児島|沖縄)$",
      message = "都道府県名を正しく入力してください")
  private String area;

  @Schema(description = "年齢 0~120まで許可", example = "25")
  @Min(value = 18, message = "年齢は18歳以上で登録してください")
  @Max(value = 120, message = "年齢は120歳以下で登録してください")
  private int age;

  @Schema(description = "性別", example = "男性")
  @Pattern(regexp = "^(男性|女性|その他)$", message = "性別は（男性｜女性｜その他）で登録してください")
  private String gender;

  @Schema(description = "備考欄", example = "10月から就活中")
  private String remark;

  @Schema(description = "論理削除フラグ", example = "false")
  @JsonProperty("isDeleted")
  private boolean isDeleted;


}
