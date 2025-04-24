package student.management.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String area;
  private int age;
  private String gender;
  private String remark;
  @JsonProperty("isDeleted")
  private boolean isDeleted;
}
