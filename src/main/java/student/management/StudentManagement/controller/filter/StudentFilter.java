package student.management.StudentManagement.controller.filter;

import org.springframework.stereotype.Component;
import student.management.StudentManagement.data.FilterParam;
import student.management.StudentManagement.domain.StudentDetail;

import java.util.List;
import java.util.Objects;

@Component
public class StudentFilter {
    public List<StudentDetail> filterStudentDetails(List<StudentDetail> studentDetailList, FilterParam param) {
        if(Objects.nonNull(param.getName())) {
            studentDetailList = studentDetailList.stream()
                    .filter(studentDetail -> studentDetail.getStudent().getName().contains(param.getName()))
                    .toList();
        }

        if(Objects.nonNull(param.getArea())) {
            studentDetailList = studentDetailList.stream()
                    .filter(studentDetail -> studentDetail.getStudent().getArea().equals(param.getArea()))
                    .toList();
        }

        if(Objects.nonNull(param.getGender())) {
            studentDetailList = studentDetailList.stream()
                    .filter(studentDetail -> studentDetail.getStudent().getGender().equals(param.getGender()))
                    .toList();
        }

        if(Objects.nonNull(param.getCourseName())) {
            studentDetailList = studentDetailList.stream()
                    .filter(studentDetail -> studentDetail.getStudentCourseWithApplicationStatusList().stream()
                            .anyMatch(studentCourseWithApplicationStatus -> studentCourseWithApplicationStatus.getStudentCourse().getCourseName().equals(param.getCourseName()))
                    ).toList();
        }
        return studentDetailList;
    }
}
