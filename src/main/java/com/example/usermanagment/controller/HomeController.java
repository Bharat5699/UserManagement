package com.example.usermanagment.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.usermanagment.dao.EmailDao;
import com.example.usermanagment.dao.UserDao;
import com.example.usermanagment.model.User;
import com.example.usermanagment.utils.EmailDetails;

@Controller
public class HomeController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	EmailDao emailDao;
	
	@GetMapping("/signin")
	public String login() {
		return "login.html";
	}
	
	@GetMapping("/signup")
	public ModelAndView register() {
		ModelAndView model=new ModelAndView();
		model.addObject(new User());
		model.setViewName("register.html");
		return model;
	}
	
	
	 @PostMapping("/registration")
	    public ModelAndView registerUserAccount(@ModelAttribute("user")  User user,
	        BindingResult result) {

	        
		 	if(user.getUsername().isEmpty()) {
	            result.rejectValue("username", null, "Please enter username");
	        }
		 	
			if(user.getName().isEmpty()) {
	            result.rejectValue("name", null, "Please enter name");
	        }
			
			if(user.getEmail().isEmpty()) {
	            result.rejectValue("email", null, "Please enter email");
	        }
			
			if(user.getMobileNo().isEmpty()) {
	            result.rejectValue("mobileNo", null, "Please enter Mobile Number");
	        }
			
			if(user.getDob()==null) {
	            result.rejectValue("dob", null, "Please enter Date of birth");
	        }
			
			if(user.getGender()==null || user.getGender().isEmpty()) {
	            result.rejectValue("gender", null, "Please enter gender");
	        }
			
			if(user.getPassword().isEmpty()) {
	            result.rejectValue("password", null, "Please enter password");
	        }
			
			if(user.getConfirmPassword().isEmpty()) {
	            result.rejectValue("confirmPassword", null, "Please enter confirm password");
	        }
		 
	        if(!user.getPassword().equals(user.getConfirmPassword())) {
	            result.rejectValue("confirmPassword", null, "Password not matched");
	        }
	        
	        ModelAndView model=new ModelAndView();
	        model.setViewName("register");
	        if (result.hasErrors()) {
	            return model;
	        }
	    	 User existingUser = userDao.findUserByUsername(user.getUsername());
	    	 if (existingUser != null) {
		            result.rejectValue("username", null, "There is already an account registered with that username");
		            return model;
	    	 }
	    	 
	    	 user.setRole("NORMAL");
	    	 user.setPassword(passwordEncoder.encode(user.getPassword()));
	    	 userDao.saveUser(user);
	    	 
	    	 
	    	 model.setViewName("register.html");
	    	 model.addObject("success", true);
	    	 model.addObject(new User());
	    	 return model;
	        	 
	    }
	 
    @GetMapping("/")
    public String home(HttpServletRequest request) {
    	
    	StringBuilder menu=new StringBuilder();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	if(authentication.getAuthorities().stream()
    	          .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
    		menu.append( "<li class='nav-item'><a class='nav-link' href='/users'>Show users</a></li>");
    	}
    	
    	
    	menu.append( "<li class='nav-item'><a class='nav-link' href='/users/profile'>Profile</a></li>");
    	menu.append( "<li class='nav-item'><a class='nav-link' href='/users/address/'>View Address</a></li>");
    	menu.append( "<li class='nav-item'><a class='nav-link' href='/users/changePassword'>Change Password</a></li>");
    	
    	HttpSession session = request.getSession();
    	session.setAttribute("menu",menu);
    	session.setAttribute("username", authentication.getName());
       return "home";
    }
    @GetMapping("/forgetPassword")
    public String forgetPassword() {
        return "forgetPassword";
    }
	
    @PostMapping("/forgetPassword")
    public String forgetPasswordAction(HttpServletRequest request,Model model) {
    	String username=request.getParameter("username");
    	User user=userDao.findUserByUsername(username);
    	if(user!=null) {
    	String charString="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    	StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(charString.charAt(random.nextInt(charString
                    .length())));
        }
        EmailDetails emailDetails=new EmailDetails();
        emailDetails.setSubject("Forget Password");
        emailDetails.setMsgBody(sb.toString());
        emailDetails.setRecipient(user.getEmail());
        boolean flag=emailDao.sendSimpleMail(emailDetails);
        if(flag) {
    	model.addAttribute("success", true);
    	user.setPassword(passwordEncoder.encode(sb.toString()));
    	userDao.saveUser(user);
        }
        else {
        	model.addAttribute("error", "something went wrong");
        }
    	}
    	else {
    		model.addAttribute("error", "username does not exist");
    	}
    	return "forgetPassword";
    }
}
