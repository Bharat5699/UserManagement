package com.example.usermanagment.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usermanagment.model.Address;
import com.example.usermanagment.repository.AddressRepository;

@Service
public class AddressDaoImpl implements AddressDao{

	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public void saveAddress(Address address) {
		addressRepository.saveAndFlush(address);
	}

	@Override
	public ArrayList<Map<String, String>> getUserAddress(String username) {
		Query query = entityManager.createQuery("from Address where users_id=:username");
		query.setParameter("username", username);
		List<Address> list=query.getResultList();
		ArrayList<Map<String, String>> mapList=new ArrayList<Map<String,String>>();
		for (Address address : list) {
			Map<String, String>  map=new HashMap<String, String>();
			map.put("line1", address.getLine1());
			map.put("line2", address.getLine2());
			map.put("state", address.getState());
			map.put("city", address.getCity());
			map.put("pincode", address.getPincode());
			String action="<a onclick='editAddress("+ address.getId() +")'><i class='fa fa-edit'></i></a><a onclick='deleteAddress("+ address.getId() +")'><i class='fa fa-trash'></i></a>";
			map.put("action", action);
			mapList.add(map);
		}
		
		return mapList;
	}

	@Override
	public Address getAddressById(int id) {
		return addressRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteAddress(int id) {
		addressRepository.deleteById(id);
		
	}

}
