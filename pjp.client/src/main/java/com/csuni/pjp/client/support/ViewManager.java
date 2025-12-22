package com.csuni.pjp.client.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
public class ViewManager implements IViewManager {
    @Getter
    private List<Scene> scenes = new ArrayList<Scene>();
    private Stage stage;

    public void goBack() {
        this.scenes.removeLast();
        updateStage();
    }

    public void addScene(Scene scene) {
        this.scenes.add(scene);
        updateStage();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void resetToFirst() {
        Scene first = this.scenes.getFirst();
        this.scenes.clear();
        this.scenes.add(first);
        updateStage();
    }

    private void updateStage() {
        double h = this.stage.getHeight();
        double w = this.stage.getWidth();
        this.stage.setScene(this.scenes.getLast());
        this.stage.setHeight(h);
        this.stage.setWidth(w);
    }
}
