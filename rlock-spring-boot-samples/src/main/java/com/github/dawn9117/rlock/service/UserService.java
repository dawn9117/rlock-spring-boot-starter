package com.github.dawn9117.rlock.service;

import com.github.dawn9117.rlock.annotation.Rlock;
import com.github.dawn9117.rlock.dao.User;
import org.springframework.stereotype.Service;

/**
 * @author HEBO
 */
@Service
public class UserService implements IUserService {


	@Rlock(keys = "#user.name")
	@Override
	public User add(User user) {
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("add:" + user);
		return user;
	}
}
