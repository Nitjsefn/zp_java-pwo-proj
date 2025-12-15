package com.csuni.pjp.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
        Application.launch(JavaFxApp.class, args);
	}

}
