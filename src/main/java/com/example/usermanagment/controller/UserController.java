package com.example.usermanagment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.usermanagment.dao.UserDao;
import com.example.usermanagment.model.User;
import com.example.usermanagment.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {	

	
	@Autowired
	UserDao userDao;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping
	public String getAllUser(Model model) {
		model.addAttribute("users", userDao.getUsers(""));
		return "users";
	}	
	
	
	@GetMapping("/profile")
	public ModelAndView viewProfile(ModelAndView model,HttpSession session) {
		model.addObject(userDao.findUserByUsername(session.getAttribute("username").toString()));
		model.setViewName("profile");
		return model;
	}
	
	@PostMapping("/profile")
	public ModelAndView updateProfile(ModelAndView model,HttpSession session,@ModelAttribute("user")  User updatedUser,
	        BindingResult result) {
		
	 	
		if(updatedUser.getName().isEmpty()) {
            result.rejectValue("name", null, "Please enter name");
        }
		
		if(updatedUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "Please enter email");
        }
		
		if(updatedUser.getMobileNo().isEmpty()) {
            result.rejectValue("mobileNo", null, "Please enter Mobile Number");
        }
		
		if(updatedUser.getDob()==null) {
            result.rejectValue("dob", null, "Please enter Date of birth");
        }
		
		if(updatedUser.getGender()==null || updatedUser.getGender().isEmpty()) {
            result.rejectValue("gender", null, "Please enter gender");
        }
		
		model.setViewName("profile");
		 if (result.hasErrors()) {
	            return model;
	        }
		 
//		User user = userDao.findUserByUsername(session.getAttribute("username").toString());
		User user = userDao.findUserByUsername(updatedUser.getUsername());
		user.setName(updatedUser.getName());
		user.setEmail(updatedUser.getEmail());
		user.setMobileNo(updatedUser.getMobileNo());
		user.setDob(updatedUser.getDob());
		user.setGender(updatedUser.getGender());
		userDao.saveUser(user);
	    model.addObject("success", true);
		
		return model;
	}
	

	@PostMapping("/updateUserData")
	public ModelAndView updateUserData(ModelAndView model,HttpServletRequest request) {
		String username=request.getParameter("editUsername");
		model.addObject(userDao.findUserByUsername(username));
		model.setViewName("profile");
		return model;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/deleteUser")
	public String deleteUser(Model model,HttpServletRequest request) {
		String username=request.getParameter("deleteUsername");
		userDao.deleteUser(username);
		model.addAttribute("users", userDao.getUsers(""));
		return "users";
	}
	
	
	@GetMapping("/changePassword")
	public String getChangePassword() {
		return "changePassword";
	}
	
	@PostMapping("/changePassword")
	public ModelAndView changePassword(ModelAndView model,HttpServletRequest request,HttpSession session,BindingResult result) {
		
		String newPassword=request.getParameter("newPassword").toString();
		String oldPassword=request.getParameter("oldPassword").toString();
		String confirmPassword=request.getParameter("confirmPassword").toString();
		model.setViewName("changePassword");
		boolean error=false;
		if(oldPassword.isEmpty()) {
			model.addObject("oldpasserror", "Enter old Password");
			error=true;
        }
		if(newPassword.isEmpty()) {
			model.addObject("newpasserror", "Enter new Password");
			error=true;
        }
		if(confirmPassword.isEmpty()) {
			model.addObject("cnfpasserror", "Enter Confirm Password");
			error=true;
        }
        if(!newPassword.equals(confirmPassword)) {
        	model.addObject("cnfpasserror",  "Password not matched");
        	error=true;
        }
        if(oldPassword.equals(newPassword)) {
        	model.addObject("newpasserror", "use different password then old one");
        	error=true;
        }

        
       String username= session.getAttribute("username").toString();
       User user=userDao.findUserByUsername(username);
       if(!passwordEncoder.matches(oldPassword,user.getPassword())) {
    	   model.addObject("oldpasserror", "Enter Valid old Password");
    	   error=true;
       }
       
       
       if(error) {
    	   return model;
       }
       
       user.setPassword(passwordEncoder.encode(newPassword));
       userDao.saveUser(user);
        model.addObject("success", true)	;	
		
		return model;
	}
	
	@PostMapping("/searchUser")
	public String searchUser(Model model,HttpServletRequest request) {
		String name=request.getParameter("name");
		model.addAttribute("users", userDao.getUsers(name));
		model.addAttribute("name", name);
		return "users";
	}
}
