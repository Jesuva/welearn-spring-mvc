package com.welearn.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.welearn.dao.CourseInterface;
import com.welearn.dao.UserDao;
import com.welearn.dao.UserInterface;
import com.welearn.model.Login;
import com.welearn.model.User;

@Controller
public class CourseController {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Autowired
	private CourseInterface courseInterface;
	
	@GetMapping("/user/enrollcourse")
	public ModelAndView enrollcourse() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("courses", courseInterface.getAllCourses());
		mv.setViewName("/user/enrollcourse");
		logger.info("Displayed Enroll-Course Page");
		return mv;
	}
	
	
	@GetMapping("/user/add-course")
	public ModelAndView showAddCoursePage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/addCourse");
		return mv;
	}
	
	@PostMapping("/user/add-course")
	public ModelAndView addCourse(HttpServletRequest req,HttpServletResponse res) throws IOException {
		
		
		try {
			HttpSession session = req.getSession(false);
			String name = req.getParameter("courseName");
			String price = req.getParameter("price");
			String chapters = req.getParameter("chapters");
			String description = req.getParameter("courseDescription");		
			String email = (String) session.getAttribute("email");
			boolean isNameAvailable = courseInterface.checkCourseName(name);
			if(isNameAvailable==true) {
				boolean isAdded = courseInterface.addCourse(name, price, description, chapters, email);
				if(isAdded) {
					
					ModelAndView mv = new ModelAndView("redirect:/user/add-course-success");
					logger.info("New Course Added");
					mv.addObject("courseTitle",name);
					return mv;

				}
				else {
					logger.info("New Course Not Added");
					return new ModelAndView("redirect:/user/add-course");
				}

			}
			else {
				ModelAndView mv = new ModelAndView();
				mv.setViewName("/user/addCourse");
				logger.info("course name not available error");
				mv.addObject("courseNameError","Course Name not available, Try some unique name");
				return mv;
				
			}
						
		} catch (Exception e) {
			logger.warning(e.getMessage());
			ModelAndView mv = new ModelAndView();
			return mv;
		}
		
		
		
	}
	
	@GetMapping ("/user/add-course-success")
	public ModelAndView showAddCourseConfirmPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/addCourseSuccess");
		logger.info("Displayed Course Added Sucess page");
		return mv;
	}
	
	@GetMapping("/user/{name}")
	public ModelAndView showCourseDetails(@PathVariable String name) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/courseDetails");
		mv.addObject("course", courseInterface.getCourseDetails(name));
		
		return mv;
	}
}
