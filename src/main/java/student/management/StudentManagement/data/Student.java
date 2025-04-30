package student.management.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  @Pattern(regexp = "^\\d+$", message = "IDは数字で入力してください")
  @Size(min = 1, max = 4, message = "IDは4桁までです")
  private String id;

  @Size(min = 2, max = 20, message = "名前は20文字以内にしてください")
  private String name;

  @Size(min = 2, max = 20, message = "カナ名は20文字以内にしてください")
  private String kanaName;

  @Size(min = 2, max = 20, message = "ニックネームは20文字以内にしてください")
  private String nickname;

  @Email(message = "正しいメールアドレスを入力してください")
  private String email;

  @Pattern(regexp = "^(北海道|青森|岩手|宮城|秋田|山形|福島|茨城|栃木|群馬|埼玉|千葉|東京|神奈川|新潟|富山|石川|福井|山梨|長野|岐阜|静岡|愛知|三重|滋賀|京都|大阪|兵庫|奈良|和歌山|鳥取|島根|岡山|広島|山口|徳島|香川|愛媛|高知|福岡|佐賀|長崎|熊本|大分|宮崎|鹿児島|沖縄)$",
      message = "都道府県名を正しく入力してください")
  private String area;

  @Min(value = 18, message = "年齢は18歳以上で登録してください")
  @Max(value = 120, message = "年齢は120歳以下で登録してください")
  private int age;

  @Pattern(regexp = "^(男性|女性|その他)$", message = "性別は（男性｜女性｜その他）で登録してください")
  private String gender;

  private String remark;

  @JsonProperty("isDeleted")
  private boolean isDeleted;
}
