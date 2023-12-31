package com.example.university.service;

import com.example.university.model.*;
import com.example.university.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class CourseJpaService implements CourseRepository {

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Override
    public ArrayList<Course> getCourses() {
        List<Course> list = courseJpaRepository.findAll();
        ArrayList<Course> courses = new ArrayList<>(list);
        return courses;
    }

    @Override
    public Course getCourseById(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course addCourse(Course course) {
        try {

            

            int professorId = course.getProfessor().getProfessorId();
            Professor professor = professorJpaRepository.findById(professorId).get();
            course.setProfessor(professor);

            List<Integer> studentIds = new ArrayList<>();
            for (Student student : course.getStudents()) {
                studentIds.add(student.getStudentId());
            }

            List<Student> students = studentJpaRepository.findAllById(studentIds);

            if (students.size() != studentIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            course.setStudents(students);

            return courseJpaRepository.save(course);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course updateCourse(int courseId, Course course) {
        try {

            Course newCourse = courseJpaRepository.findById(courseId).get();

            if (course.getCourseName() != null) {
                newCourse.setCourseName(course.getCourseName());
            }

            if (course.getCredits() != 0) {
                newCourse.setCredits(course.getCredits());
            }

            if (course.getProfessor() != null) {
                int professorId = course.getProfessor().getProfessorId();
                Professor professor = professorJpaRepository.findById(professorId).get();
                course.setProfessor(professor);
            }

            if (course.getStudents() != null) {
                List<Integer> studentIds = new ArrayList<>();
                for (Student student : course.getStudents()) {
                    studentIds.add(student.getStudentId());
                }
                List<Student> students = studentJpaRepository.findAllById(studentIds);
                if (students.size() != studentIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                newCourse.setStudents(students);
            }

            return courseJpaRepository.save(newCourse);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        try {
            courseJpaRepository.deleteById(courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public Professor getProfessorCourse(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            Professor professor = course.getProfessor();
            return professor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Student> getStudentCourses(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getStudents();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}