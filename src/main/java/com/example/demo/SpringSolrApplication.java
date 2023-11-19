package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringSolrApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringSolrApplication.class, args);
	}

	@EventListener
	public void handleContextRefreshEvent(ContextStartedEvent ctxStartEvt) {
		System.out.println("Context Start Event received.");
	}

}
