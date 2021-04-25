package com.welearn.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	@Autowired
	private CourseInterface courseinterface;
	
	@GetMapping("/user/enrollcourse")
	public ModelAndView enrollcourse() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("courses", courseinterface.getAllCourses());
		mv.setViewName("/user/enrollcourse");
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
			String name = req.getParameter("courseName");
			String price = req.getParameter("price");
			String chapters = req.getParameter("chapters");
			String description = req.getParameter("courseDescription");			
			boolean isNameAvailable = courseinterface.checkCourseName(name);
			if(isNameAvailable==true) {
				boolean isAdded = courseinterface.addCourse(name, price, description, chapters);
				if(isAdded) {
					
					ModelAndView mv = new ModelAndView("redirect:/user/add-course-success");
					mv.addObject("courseTitle",name);
					return mv;

				}
				else {
					System.out.println("not added");
					return new ModelAndView("redirect:/user/add-course");
				}

			}
			else {
				ModelAndView mv = new ModelAndView();
				mv.setViewName("/user/addCourse");
				System.out.println("not added e");
				mv.addObject("courseNameError","Course Name not available, Try some unique name");
				return mv;
				
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mv = new ModelAndView();
			return mv;
		}
		
		
		
	}
	
	@GetMapping ("/user/add-course-success")
	public ModelAndView showAddCourseConfirmPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/addCourseSuccess");
		return mv;
	}
	
	@GetMapping("/user/{name}")
	public ModelAndView showCourseDetails(@PathVariable String name) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/courseDetails");
		mv.addObject("course", courseinterface.getCourseDetails(name));
		
		return mv;
	}
}
