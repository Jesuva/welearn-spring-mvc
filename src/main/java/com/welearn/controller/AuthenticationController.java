package com.welearn.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.welearn.dao.UserInterface;
import com.welearn.model.Login;
import com.welearn.model.User;

import jakarta.validation.Valid;
@Controller
public class AuthenticationController {

	@Autowired
	private UserInterface userinterface;
	
	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
	
	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
	
	@PostMapping("/login")
	public ModelAndView login(@RequestParam("userMail") String email,@RequestParam("userPassword") String password,HttpServletRequest request ) {
		Login userlogin = userinterface.findUser(email,password);
		ModelAndView mv = new ModelAndView();
		mv.clear();
		if(userlogin!=null) {
			HttpSession session =request.getSession();
			session.setAttribute("name",userlogin.getName());
			session.setAttribute("email", userlogin.getEmail());
			
			return new ModelAndView("redirect:/user/enrollcourse");
			
		}
		else {
			mv.setViewName("index");
			mv.addObject("loginError","Invalid Credentials!");
			return mv;
		}
		
	}
	
	@GetMapping("/signup")
	public ModelAndView signup(Model model) {
		model.addAttribute("user", new User());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("signup");
		return mv;
	}
	
	@PostMapping("/signup")
	public ModelAndView signup(HttpServletRequest req,@Valid User user, BindingResult br) {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		System.out.println(br.hasErrors());
		ModelAndView mv = new ModelAndView();
		boolean emailCheck = Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",email);
		boolean passwordCheck = Pattern.matches("^(?=.*[a-z]{2})(?=.*[A-Z]{2})(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]{2}).{6,20}$", password);
		mv.clear();
		if(emailCheck==false) {
			mv.addObject("emailError","Please Enter Valid Email");
		}
		if(passwordCheck==false) {
			mv.addObject("passwordError","Please Use Strong Password");
		}
		
		if(userinterface.checkUserMail(email)==true && emailCheck==true && passwordCheck==true) {
			HttpSession session = req.getSession();
			session.setAttribute("name", name);
			session.setAttribute("email", email);			
			userinterface.addUser(name,email,password);
			return new ModelAndView("redirect:/user/enrollcourse");
		}
		if(userinterface.checkUserMail(email)==false) {
			mv.addObject("emailError","Email Already Exists, Try Login!");

		}
		return mv;
		
		
	}
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			session.invalidate();
			response.sendRedirect("/WeLearn/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
