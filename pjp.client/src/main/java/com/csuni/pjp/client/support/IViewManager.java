package com.csuni.pjp.client.support;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface IViewManager {
    void setStage(Stage stage);
    void goBack();
    void addScene(Scene scene);
    void resetToFirst();
}
