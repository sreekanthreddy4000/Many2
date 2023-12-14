package com.example.university.repository;

import com.example.university.model.*;
import java.util.*;

public interface CourseRepository {
    ArrayList<Course> getCourses();

    Course getCourseById(int courseId);

    Course addCourse(Course course);

    Course updateCourse(int courseId, Course course);

    void deleteCourse(int courseId);

    Professor getProfessorCourse(int courseId);

    List<Student> getStudentCourses(int courseId);

}