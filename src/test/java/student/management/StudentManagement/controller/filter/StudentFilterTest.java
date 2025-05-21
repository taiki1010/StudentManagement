package student.management.StudentManagement.controller.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.management.StudentManagement.data.*;
import student.management.StudentManagement.domain.StudentCourseWithApplicationStatus;
import student.management.StudentManagement.domain.StudentDetail;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentFilterTest {

    private StudentFilter sut;

    private Student student;
    private StudentCourse studentCourse;
    private ApplicationStatus applicationStatus;
    private StudentCourseWithApplicationStatus studentCourseWithApplicationStatus;
    private List<StudentCourse> studentCourseList;
    private List<StudentCourseWithApplicationStatus> studentCourseWithApplicationStatusList;
    private StudentDetail studentDetail;
    private List<StudentDetail> studentDetailList;
    private LocalDateTime now;

    @BeforeEach
    void before() {
        sut = new StudentFilter();
        createSampleStudentDetail();
    }

    private void createSampleStudentDetail() {
        now = LocalDateTime.of(2025, 5, 6, 14, 0);
        student = new Student("1", "山田太郎", "ヤマダタロウ", "やまちゃん",
                "yamada@example.com", "東京", 30, "男性", null, false);
        studentCourse = new StudentCourse("1", "1", "Javaフルコース", now,
                now.plusMonths(3));
        applicationStatus = new ApplicationStatus("1", "1", Status.TEMPORARY_APPLICATION.getStatus());
        studentCourseWithApplicationStatus = new StudentCourseWithApplicationStatus(studentCourse,
                applicationStatus);
        studentCourseWithApplicationStatusList = List.of(studentCourseWithApplicationStatus);
        studentDetail = new StudentDetail(student, studentCourseWithApplicationStatusList);
        studentDetailList = List.of(studentDetail);
    }

    @Test
    void 名前パラメータに該当する場合受講生詳細リストが返却されること() {
        FilterParam param = new FilterParam("山田", null, null, null);
        List<StudentDetail> actual = sut.filterStudentDetails(studentDetailList, param);
        assertEquals(studentDetailList, actual);
    }

    @Test
    void 地域パラメータに該当する場合受講生詳細リストが返却されること() {
        FilterParam param = new FilterParam(null, "東京", null, null);
        List<StudentDetail> actual = sut.filterStudentDetails(studentDetailList, param);
        assertEquals(studentDetailList, actual);
    }

    @Test
    void 性別パラメータに該当する場合受講生詳細リストが返却されること() {
        FilterParam param = new FilterParam(null, null, "男性", null);
        List<StudentDetail> actual = sut.filterStudentDetails(studentDetailList, param);
        assertEquals(studentDetailList, actual);
    }

    @Test
    void コース名パラメータに該当する場合受講生詳細リストが返却されること() {
        FilterParam param = new FilterParam(null, null, null, "Javaフルコース");
        List<StudentDetail> actual = sut.filterStudentDetails(studentDetailList, param);
        assertEquals(studentDetailList, actual);
    }

    @Test
    void パラメータがすべてnullの場合受講生詳細リストが返却されること() {
        FilterParam param = new FilterParam(null, null, null, null);
        List<StudentDetail> actual = sut.filterStudentDetails(studentDetailList, param);
        assertEquals(studentDetailList, actual);
    }

    @Test
    void パラメータに該当する情報が存在しない場合空のリストが返却されること() {
        FilterParam param = new FilterParam("野口", null, null, null);
        List<StudentDetail> expected = List.of();
        List<StudentDetail> actual = sut.filterStudentDetails(studentDetailList, param);
        assertEquals(expected, actual);
    }
}
