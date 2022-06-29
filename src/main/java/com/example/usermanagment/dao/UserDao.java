package com.example.usermanagment.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.transaction.Transactional;

import com.example.usermanagment.model.User;

public interface UserDao {
	
	public User findUserByUsername(String username);
	
	@Transactional
	public void saveUser(User user);

	public ArrayList<Map<String, String>> getUsers(String name);
	
	@Transactional
	public void deleteUser(String username);
}
