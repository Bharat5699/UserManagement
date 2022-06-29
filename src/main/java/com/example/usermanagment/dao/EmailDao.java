package com.example.usermanagment.dao;

import com.example.usermanagment.utils.EmailDetails;

public interface EmailDao {
	public  Boolean sendSimpleMail(EmailDetails details);
}
