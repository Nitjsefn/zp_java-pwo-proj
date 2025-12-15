package com.csuni.pjp.client;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class JavaFxApp extends Application {
    private ConfigurableApplicationContext appContext;


    @Override
    public void init() {
        appContext = new SpringApplicationBuilder(Main.class).run();
    }

    @Override
    public void stop() {
        appContext.close();
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new Group());
        Paint paint = Paint.valueOf("#212121");
        scene.setFill(paint);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
