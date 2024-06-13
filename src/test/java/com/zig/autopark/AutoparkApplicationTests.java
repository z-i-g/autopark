package com.zig.autopark;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;


class AutoparkApplicationTests {


	@Test
	void contextLoads() {
		System.out.println(new BCryptPasswordEncoder().encode("man2"));
	}

	@Test
	void dateTest() {
		TimeZone timeZone = TimeZone.getDefault();
		System.out.println(Date.from(Instant.now()));
	}


}
