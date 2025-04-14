package com.workintech.spring17challenge.validation;

import com.workintech.spring17challenge.exceptions.ApiException;
import com.workintech.spring17challenge.model.Course;
import org.springframework.http.HttpStatus;

import java.util.List;

public class CourseValidation {
    public static void isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ApiException(name + "can not find", HttpStatus.NOT_FOUND);
        }
    }

    public static void isValidId(Integer id) {
        if (id == null || id < 1) {
            throw new ApiException(id + " is not greater than 0 or is not valid", HttpStatus.BAD_REQUEST);
        }
    }


    public static void wrongCreditNumber(Integer credit)
    {
        if(credit > 4){
            throw new ApiException("Wrong credit value",HttpStatus.BAD_REQUEST);
        }
    }



    public  static void courseNullControl(Course course)
    {
        if(course == null)
        {
            throw new ApiException("Course is not null", HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkCourseExistence(List<Course> courses, String name, boolean existence){
        for(Course course : courses){
            if(course.getName().equals(name)){
                existence = true;
            }
        }
        if(courses.isEmpty()){
            return;
        }

        if(existence){
            throw new ApiException("Course is already exist: " + name, HttpStatus.BAD_REQUEST);
        }
    }
}


