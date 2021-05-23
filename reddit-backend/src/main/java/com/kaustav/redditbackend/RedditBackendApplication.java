package com.kaustav.redditbackend;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class RedditBackendApplication
{

	public static void main(String[] args) {
		SpringApplication.run(RedditBackendApplication.class, args);
	}

}
