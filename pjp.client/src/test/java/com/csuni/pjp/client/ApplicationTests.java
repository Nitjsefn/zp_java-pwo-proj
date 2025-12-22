package com.csuni.pjp.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testfx.framework.junit.ApplicationTest;


import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@SpringBootTest
class ApplicationTests extends ApplicationTest {
    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new Pane()));
    }


	@Test
	void contextLoads() {
	}

}
