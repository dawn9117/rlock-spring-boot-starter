package com.github.dawn9117.rlock;

import com.github.dawn9117.rlock.dao.User;
import com.github.dawn9117.rlock.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.IntStream;

@SpringBootApplication
public class RlockSpringBootSamplesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RlockSpringBootSamplesApplication.class, args);
	}

	@Autowired
	private IUserService userService;

	@Override
	public void run(String... args) throws Exception {
		IntStream.range(1,3).parallel().forEach(i -> {
			User user = new User();
			user.setName("zhangsan");
			userService.add(user);
		});
	}
}
