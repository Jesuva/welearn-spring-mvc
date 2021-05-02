package com.welearn.dao;

import java.util.List;

import com.welearn.model.Course;

public interface CourseInterface {
	
	public List<Course> getAllCourses();
	
	public List<Course> getAllAvailableCourses(String email);
	
	public List<Course> getAllEnrolledCourses(String email);
	
	public boolean checkCourseName(String name);
	
	public boolean addCourse(String name,String price,String description,String chapters,String email);
	
	public boolean updateCourse(String name,String price,String description,String chapters,int courseId);
	
	public Course getCourseDetails(int courseId);
	
	public List<Course> getCourseCreatedByUser(String name);
	
	public Course enrollCourse(String name, int courseId);
	
	public Course unenrollCourse(String email, int courseId);
	
	
}