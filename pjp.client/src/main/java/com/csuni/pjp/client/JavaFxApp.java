package com.csuni.pjp.client;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.csuni.pjp.client.scenes.GameJoinScene;
import com.csuni.pjp.client.scenes.LoginScene;
import com.csuni.pjp.client.scenes.RegisterScene;
import com.csuni.pjp.client.support.IViewManager;
import com.csuni.pjp.client.support.TimersRegistry;
import com.csuni.pjp.client.viewModels.LoginViewModel;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class JavaFxApp extends Application {
    private ConfigurableApplicationContext appContext;
    private IViewManager viewManager;


    @Override
    public void init() {
        appContext = new SpringApplicationBuilder(Main.class).run();
        viewManager = appContext.getBean(IViewManager.class);
    }

    @Override
    public void stop() {
        appContext.close();
    }

    @Override
    public void start(Stage primaryStage) {
        viewManager.setStage(primaryStage);
        //viewManager.addScene(new LoginScene(appContext.getBean(LoginViewModel.class)));
        viewManager.addScene(appContext.getBean(LoginScene.class));
        
        primaryStage.setOnCloseRequest(event -> onAppClose());
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    private void onAppClose() {
        TimersRegistry registry = appContext.getBean(TimersRegistry.class);
        registry.cancelAll();
    }
}
