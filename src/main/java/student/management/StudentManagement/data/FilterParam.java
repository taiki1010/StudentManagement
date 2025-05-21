package student.management.StudentManagement.data;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class FilterParam {

    @Size(min = 2, max = 20, message = "氏名は2文字以上、20文字以内にしてください")
    private String name;

    @Pattern(regexp = "^(北海道|青森|岩手|宮城|秋田|山形|福島|茨城|栃木|群馬|埼玉|千葉|東京|神奈川|新潟|富山|石川|福井|山梨|長野|岐阜|静岡|愛知|三重|滋賀|京都|大阪|兵庫|奈良|和歌山|鳥取|島根|岡山|広島|山口|徳島|香川|愛媛|高知|福岡|佐賀|長崎|熊本|大分|宮崎|鹿児島|沖縄)$",
            message = "都道府県名を正しく入力してください")
    private String area;

    @Pattern(regexp = "^(男性|女性|その他)$", message = "性別は（男性｜女性｜その他）で登録してください")
    private String gender;

    @Pattern(regexp = "^(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)$",
            message = "コース名は(Javaフルコース|AWSコース|デザインコース|WP副業コース|Webマーケティングコース|フロントエンドコース|映像制作コース|英会話コース)から選択してください")
    private String courseName;

    public boolean isAllFieldNull () {
        return Objects.isNull(name) && Objects.isNull(area) && Objects.isNull(gender) && Objects.isNull(courseName);
    }
}
