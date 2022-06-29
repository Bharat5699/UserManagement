package com.example.usermanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagment.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

}
