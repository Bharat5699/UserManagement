package com.example.usermanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.usermanagment.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
