package com.example.usermanagment.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.example.usermanagment.model.Address;

public interface AddressDao {

	@Transactional
	public void saveAddress(Address address);
	public ArrayList<Map<String, String>> getUserAddress(String username);
	public Address getAddressById(int id);
	
	@Transactional
	public void deleteAddress(int id);
	
}
