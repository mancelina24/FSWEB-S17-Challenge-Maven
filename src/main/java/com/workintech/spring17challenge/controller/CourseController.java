package com.workintech.spring17challenge.controller;


import com.workintech.spring17challenge.entity.HighCourseGpa;
import com.workintech.spring17challenge.entity.LowCourseGpa;
import com.workintech.spring17challenge.entity.MediumCourseGpa;
import com.workintech.spring17challenge.exceptions.*;
import com.workintech.spring17challenge.model.Course;
import com.workintech.spring17challenge.model.CourseGpa;
import com.workintech.spring17challenge.model.CourseResponse;
import com.workintech.spring17challenge.validation.CourseValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private List<Course> courses;

    private LowCourseGpa lowCourseGpa;
    private MediumCourseGpa mediumCourseGpa;
    private HighCourseGpa highCourseGpa;

    @Autowired
    public CourseController(LowCourseGpa lowCourseGpa, MediumCourseGpa mediumCourseGpa, HighCourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @PostConstruct
    public void init() {
        courses = new ArrayList<>();
    }

    @GetMapping
    public List<Course> getCourses() {
        return this.courses;
    }

    @GetMapping("/{name}")
    public Course getCoursesByName(@PathVariable String name){
        for(Course course: courses){
            if(course.getName().equalsIgnoreCase(name)){
                return course;
            }
        }
        throw new ApiException("Course cannot found.", HttpStatus.NOT_FOUND);
    }

    private void validateCourse(Course course) {
        if (course.getCredit() <= 0 || course.getCredit() > 4) {
            throw new InvalidCourseCreditException("Course credit must be between 1 and 4.");
        }
    }

    private double calculateTotalGpa(Course course) {
        int credit = course.getCredit();
        int coefficient = course.getGrade().getCoefficient();
        double gpaValue;

        if (credit <= 2) {
            gpaValue = lowCourseGpa.getGpa();
        } else if (credit == 3) {
            gpaValue = mediumCourseGpa.getGpa();
        } else if (credit == 4) {
            gpaValue = highCourseGpa.getGpa();
        } else {
            throw new InvalidCourseCreditException("Invalid course credit: " + credit); // Should not happen due to validation but for safety
        }
        return coefficient * credit * gpaValue;
    }

    @PostMapping
    public ResponseEntity<CourseResponse>  saveCourse(@RequestBody Course course){
        courses.add(course);

        double totalGpa = 0;
        if(course.getCredit() == null){
            throw new ApiException("Course cannot be null", HttpStatus.BAD_REQUEST);
        } else if(course.getCredit() <= 2){
            totalGpa = (course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa());
        } else if (course.getCredit() == 3) {
            totalGpa = (course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa());
        } else if(course.getCredit() == 4){
            totalGpa = (course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa());
        }

        CourseResponse courseResponse = new CourseResponse(course, totalGpa);
        return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        validateCourse(updatedCourse);
        if (id < 0 || id >= courses.size()) {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        courses.set(id, updatedCourse);
        double totalGpa = calculateTotalGpa(updatedCourse);
        CourseResponse response = new CourseResponse(updatedCourse, totalGpa);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public Course delete(@PathVariable Integer id)
    {
        CourseValidation.isValidId(id);
        Course course = courses.get(id);
        CourseValidation.courseNullControl(course);
        courses.remove(id);
        return course;
    }
}
