package com.example.usermanagment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.usermanagment.dao.AddressDao;
import com.example.usermanagment.dao.UserDao;
import com.example.usermanagment.model.Address;
import com.example.usermanagment.model.User;

@Controller
@RequestMapping("/users/address")
public class AddressController {

	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping
	public ModelAndView getAddressPage(ModelAndView model, HttpServletRequest request) { 
		
		model.addObject("addressList", addressDao.getUserAddress(request.getSession().getAttribute("username").toString()));
		model.setViewName("address");
		return model;
	}
	
	   @PostMapping
	   public ModelAndView addUserAddress(HttpServletRequest request) {
		   int count=Integer.parseInt(request.getParameter("count"));
		        
		   
//			 	if(address.getLine1().isEmpty()) {
//		            result.rejectValue("line1", null, "Please enter Line 1");
//		        }
//			 	
//			 	if(address.getLine2().isEmpty()) {
//			 		result.rejectValue("line2", null, "Please enter Line 2");
//		        }
//				
//			 	if(address.getState().isEmpty()) {
//		            result.rejectValue("state", null, "Please enter State");
//		        }
//				
//			 	if(address.getCity().isEmpty()) {
//		            result.rejectValue("city", null, "Please enter City");
//		        }
//				
//			 	if(address.getPincode().isEmpty()) {
//		            result.rejectValue("pincode", null, "Please enter Pincode");
//		        }
//				
				
			 	HttpSession session = request.getSession();
		        ModelAndView model=new ModelAndView();
		        model.setViewName("address");
//		        if (result.hasErrors()) {
//		        	model.addObject("addressList", addressDao.getUserAddress(session.getAttribute("username").toString()));
//		            return model;
//		        }
		    	 
		       
		        User user=userDao.findUserByUsername(session.getAttribute("username").toString());
		        
		        for(int i=1;i<=count;i++) {
		        Address address=new Address();
		        address.setUser(user);
		        address.setLine1(request.getParameter("line1"+i));
		        address.setLine2(request.getParameter("line2"+i));
		        address.setState(request.getParameter("state"+i));
		        address.setCity(request.getParameter("city"+i));
		        address.setPincode(request.getParameter("pincode"+i));
		        addressDao.saveAddress(address);
		    	 
		        }
		         model.addObject("addressList", addressDao.getUserAddress(session.getAttribute("username").toString()));
		    	 model.addObject("success", true);
		    	 return model;
		        	 
		    }
		 
	   @PostMapping("/updateAddress")
		public ModelAndView updateAddress(ModelAndView model, HttpServletRequest request) { 
			String id=request.getParameter("editAddressId").toString();
			model.addObject(addressDao.getAddressById(Integer.parseInt(id)));
			model.setViewName("editAddress");
			return model;
		}
	   
	   @PostMapping("/updateAddressAction")
		public ModelAndView updateAddressAction(@ModelAttribute("address")  Address updatedAddress,HttpServletRequest request,
		        BindingResult result) { 
		   if(updatedAddress.getLine1().isEmpty()) {
	            result.rejectValue("line1", null, "Please enter Line 1");
	        }
		 	
		 	if(updatedAddress.getLine2().isEmpty()) {
		 		result.rejectValue("line2", null, "Please enter Line 2");
	        }
			
		 	if(updatedAddress.getState().isEmpty()) {
	            result.rejectValue("state", null, "Please enter State");
	        }
			
		 	if(updatedAddress.getCity().isEmpty()) {
	            result.rejectValue("city", null, "Please enter City");
	        }
			
		 	if(updatedAddress.getPincode().isEmpty()) {
	            result.rejectValue("pincode", null, "Please enter Pincode");
	        }
			
			
	        ModelAndView model=new ModelAndView();
	        model.setViewName("address");
	        if (result.hasErrors()) {
	            return model;
	        }
	    	 
	       Address address=addressDao.getAddressById(updatedAddress.getId());
	       address.setLine1(updatedAddress.getLine1());
	       address.setLine2(updatedAddress.getLine2());
	       address.setState(updatedAddress.getState());
	       address.setCity(updatedAddress.getCity());
	       address.setPincode(updatedAddress.getPincode());
	       
	        addressDao.saveAddress(address);
	        model.addObject("addressList", addressDao.getUserAddress(request.getSession().getAttribute("username").toString()));
	        model.addObject("msg", "Address Updated successFully");
//	    	 model.addObject("success", true);
//	    	 model.addObject(address);
	    	 return model;
		}
	   
	   @PostMapping("/deleteAddress")
		public ModelAndView deleteAddress(ModelAndView model, HttpServletRequest request) { 
			String id=request.getParameter("deleteAddressId").toString();
			addressDao.deleteAddress(Integer.parseInt(id));
			model.addObject(new Address());
			model.addObject("addressList", addressDao.getUserAddress(request.getSession().getAttribute("username").toString()));
			model.setViewName("address");
			return model;
		}
	
}
