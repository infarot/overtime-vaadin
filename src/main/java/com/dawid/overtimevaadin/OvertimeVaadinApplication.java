package com.dawid.overtimevaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class OvertimeVaadinApplication {
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
	}
	public static void main(String[] args) {
		SpringApplication.run(OvertimeVaadinApplication.class, args);
	}

}
