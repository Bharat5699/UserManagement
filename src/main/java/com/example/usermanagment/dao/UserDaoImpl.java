package com.example.usermanagment.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usermanagment.model.User;
import com.example.usermanagment.repository.UserRepository;

@Service
public class UserDaoImpl implements UserDao{
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired 
	 private EntityManager entityManager;

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findById(username).orElse(null);
	}

	@Override
	public void saveUser(User user) {
		userRepository.saveAndFlush(user);
	}

	@Override
	public ArrayList<Map<String, String>> getUsers(String name) {
		StringBuilder  qry = new StringBuilder();
		qry.append("from User u where role!='ADMIN' ");
		if(name!=null && !name.equals("")) {
			qry.append(" and name like :name ");
		}
		
			Query query = entityManager.createQuery(qry.toString());
			if(name!=null && !name.equals("")) {
				query.setParameter("name", name+"%");
			}
			List<User> list=query.getResultList();
			ArrayList<Map<String, String>> mapList=new ArrayList<Map<String,String>>();
			for (User user : list) {
				Map<String, String>  map=new HashMap<String, String>();
				map.put("name", user.getName());
				map.put("dob", user.getDob().toGMTString());
				map.put("mobileNo", user.getMobileNo());
				map.put("gender", user.getGender());
				map.put("email", user.getEmail());
				String action="<a onclick='editUser(\""+ user.getUsername() +"\")'><i class='fa fa-edit'></i></a><a onclick='deleteUser(\""+ user.getUsername() +"\")'><i class='fa fa-trash'></i></a>";
				map.put("action", action);
				mapList.add(map);
			}
			
			return mapList;
		 
	}

	@Override
	public void deleteUser(String username) {
		userRepository.deleteById(username);
		
	}
	
	

}
