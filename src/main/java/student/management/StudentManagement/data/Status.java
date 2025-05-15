package student.management.StudentManagement.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  TEMPORARY_APPLICATION("仮申込"),
  FORMAL_APPLICATION("本申込"),
  IN_PROGRESS("受講中"),
  COMPLETED("受講修了");

  private final String status;

}
