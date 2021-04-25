package com.welearn.dao;

import java.util.List;

import com.welearn.model.Course;

public interface CourseInterface {
	public List<Course> getAllCourses();
	public boolean checkCourseName(String name);
	public boolean addCourse(String name,String price,String description,String chapters);
	public Course getCourseDetails(String courseName);
}
