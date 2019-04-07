package com.imagetracker.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import com.imagetracker.util.FileStorageProperties;

@SpringBootApplication(scanBasePackages = { "com.imagetracker" })
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ImageTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageTrackerApplication.class, args);
	}

}
