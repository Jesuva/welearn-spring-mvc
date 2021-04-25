package com.welearn.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.welearn.dao.CourseInterface;
import com.welearn.mapper.CourseMapper;
import com.welearn.mapper.LoginMapper;
import com.welearn.model.Course;
import com.welearn.model.Login;

@Service
public class CourseServices extends JdbcDaoSupport implements CourseInterface {
	
	@Autowired
    public CourseServices (DataSource datasource) {
        this.setDataSource(datasource);
    }
	
	public List<Course> getAllCourses() {
		try {
			String sql = "SELECT * FROM courses;";
			Object[] params = new Object[] {  };
	        CourseMapper mapper = new CourseMapper();
	        List<Course> courseInfo = this.getJdbcTemplate().query(sql, params, mapper);
            return courseInfo;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addCourse(String name,String price,String description ,String chapters, String email) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(name.getBytes());
			BigInteger hash = new BigInteger(1,md.digest());
			String hashCourseName = hash.toString(16);
			String sql = "INSERT INTO `welearn`.`courses` (`courseId`, `courseName`, `courseDescription`, `chapters`, `coursePrice`, `created_by`) VALUES (?,?,?,?,?,?);\r\n"
					+ ";";
			Object[] params = new Object[] {hashCourseName,name,description,chapters,price,email};
			int addCourse = this.getJdbcTemplate().update(sql, params);
			if(addCourse!=0) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkCourseName(String name) {
		String sql ="SELECT count(*) FROM welearn.courses where courseName=?;";
		Object[] params = new Object[] {name};
		int courseCount = this.getJdbcTemplate().queryForObject(sql, params,Integer.class);
		if(courseCount==0) {
			return true;
		}
		
		return false;
	}

	public Course getCourseDetails(String courseName) {
		String sql = "select * from courses where courseName=?;";
		Object[] params = new Object[] {courseName};
		CourseMapper mapper = new CourseMapper();
		Course course = this.getJdbcTemplate().queryForObject(sql, params,mapper);
		return course;
	}
}
