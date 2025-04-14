package com.workintech.spring17challenge.model;

import com.fasterxml.jackson.databind.node.DoubleNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseResponse {
private Course course;
private Double totalGpa;
}
