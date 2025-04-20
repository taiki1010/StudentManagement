package student.management.StudentManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private String name = "Enami Kouji";
	private String age = "37";

	private Map<String ,String> studentMap;

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo() {
		return name + " " + age + "歳";
	}

	@GetMapping("/age")
	public String getAge() {
		return age;
	}

	@PostMapping("/studentInfo")
	public void setStudentInfo(String name, String age) {
		this.name = name;
		this.age = age;
	}

	@PostMapping("/studentName")
	public void updateStudentName(String name) {
		this.name = name;
	}

	@GetMapping("/studentMap")
		public String getStudentMap() {
			if(studentMap == null || studentMap.isEmpty()) {
				return Message.ERROR_NO_STUDENT_IN_DATA;
			}
		String students = studentMap.entrySet().stream()
				.map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining("\n"));
			return students;
		}

	@PostMapping("/studentMap")
	public String addStudentInMap(String id, String name) {
		if(studentMap == null) {
			studentMap = new HashMap<>();
		}
		if(studentMap.containsKey(id)) {
			return Message.ERROR_DUPLICATED_ID;
		}
		this.studentMap.put(id, name);
		return "[追加された生徒]" + id + ":" + name;
	}

	@PatchMapping("/studentMap")
	public String modifyStudentName(String id, String name) {
		if(studentMap == null || studentMap.isEmpty()) {
			return Message.ERROR_NO_STUDENT_IN_DATA;
		} else if(!studentMap.containsKey(id)) {
			return Message.ERROR_NOT_EXIST_STUDENT;
		}
		studentMap.computeIfPresent(id, (key, oldValue) -> name);
		return "[変更された生徒]" + id + ":" + name;
	}

	@DeleteMapping("/studentMap")
	public String deleteStudent(String id) {
		if(studentMap == null || studentMap.isEmpty()) {
			return Message.ERROR_NO_STUDENT_IN_DATA;
		} else if(!studentMap.containsKey(id)) {
			return Message.ERROR_NOT_EXIST_STUDENT;
		}
		String removedStudent = studentMap.entrySet().stream()
				.filter(student -> student.getKey().equals(id))
				.map(student -> student.getKey() + ":" + student.getValue())
				.collect(Collectors.joining());

		studentMap.remove(id);
		return "[削除された生徒]" + removedStudent;
	}

}
