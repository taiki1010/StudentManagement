package student.management.StudentManagement.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  TemporaryApplication("仮申込"),
  FormalApplication("本申込"),
  InProgress("受講中"),
  Completed("受講修了");

  private final String status;

}
