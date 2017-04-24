package de.bst.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PeopleApplication {

	public static final String FEED_PEOPLE_LIST = "people-feed";
	public static final String FEED_PEOPLE_UPDATED = "people-feed-updated";

	public static void main(String[] args) {
		SpringApplication.run(PeopleApplication.class, args);
	}
}
