package com.welearn.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

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
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public List<Course> getAllAvailableCourses(String email){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "SELECT * FROM courses where courseId Not in (select course_id from course_enrollment where user_id=?);";
			Object[] params = new Object[] { userId };
	        CourseMapper mapper = new CourseMapper();
	        List<Course> courseInfo = this.getJdbcTemplate().query(sql, params, mapper);
            return courseInfo;
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public List<Course> getAllEnrolledCourses(String email){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "SELECT * FROM courses where courseId IN (select course_id from course_enrollment where user_id=?);";
			Object[] params = new Object[] { userId };
	        CourseMapper mapper = new CourseMapper();
	        List<Course> courseInfo = this.getJdbcTemplate().query(sql, params, mapper);
            return courseInfo;
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
		
	}
	public boolean addCourse(String name,String price,String description ,String chapters, String email) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "INSERT INTO `welearn`.`courses` (`courseName`, `courseDescription`, `chapters`, `coursePrice`, `created_by`) VALUES (?,?,?,?,?);\r\n"
					+ ";";
			Object[] params = new Object[] {name,description,chapters,price,userId};
			int addCourse = this.getJdbcTemplate().update(sql, params);
			if(addCourse!=0) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
			return false;
		}
	}
	
	public boolean updateCourse(String name,String price,String description ,String chapters,int courseId) {
		try {
			String sql = "UPDATE `welearn`.`courses` SET `courseName` = ?,`coursePrice` = ?,`chapters`=?,`courseDescription`=? WHERE (`courseId` = ?);";
			int updateCourse = this.getJdbcTemplate().update(sql,name,price,chapters,description,courseId);
			if(updateCourse!=0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception e) {
			
			logger.warning(e.getMessage());
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

	public Course getCourseDetails(int courseId) {
		String sql = "select * from courses where courseId=?;";
		Object[] params = new Object[] {courseId};
		CourseMapper mapper = new CourseMapper();
		Course course = this.getJdbcTemplate().queryForObject(sql, params,mapper);
		return course;
	}
	
	public List<Course> getCourseCreatedByUser(String name){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(name.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "select * from courses where created_by=?;";
			Object[] params = new Object[] {userId};
			CourseMapper mapper = new CourseMapper();
			List<Course> courses = this.getJdbcTemplate().query(sql, params,mapper);
			return courses;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Course enrollCourse(String email,int courseId) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "INSERT INTO `welearn`.`course_enrollment` (`course_id`, `user_id`) VALUES (?, ?);\r\n"
					+ "";
			Object[] params = new Object[] {courseId,userId};
			int addEnrollment = this.getJdbcTemplate().update(sql,params);
			if(addEnrollment!=0) {
				String courseDetailsSql = "select * from courses where courseId=?;";
				Object[] thisParams = new Object[] {courseId};
				CourseMapper mapper = new CourseMapper();
				Course course = this.getJdbcTemplate().queryForObject(courseDetailsSql, thisParams,mapper);
				return course;
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Course unenrollCourse(String email, int courseId) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "INSERT INTO `welearn`.`course_unenrollment` (`user_id`,`course_id`) VALUES (?, ?);\r\n"
					+ "";
			Object[] params = new Object[] {userId,courseId};
			int isUnenrolled = this.getJdbcTemplate().update(sql,params);
			if (isUnenrolled!=0) {
				String sql3 = "DELETE FROM `course_enrollment` WHERE (`user_id` = ? and `course_id`=?);";
				Object[] params3 = new Object[] {userId,courseId};
				this.getJdbcTemplate().update(sql3,params3);
				String sql2 = "select * from `courses` where `courseId`=?;";
				Object[] thisParams = new Object[] {courseId};
				CourseMapper mapper = new CourseMapper();
				Course course = this.getJdbcTemplate().queryForObject(sql2, thisParams,mapper);
				return course;
			}
			else {
				logger.warning("update query not working");
				return null;
			}
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
			return null;
		}
	}
}
