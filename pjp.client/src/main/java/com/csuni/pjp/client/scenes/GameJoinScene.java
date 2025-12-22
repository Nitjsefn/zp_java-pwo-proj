package com.csuni.pjp.client.scenes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.support.IViewManager;
import com.csuni.pjp.client.viewModels.GameJoinViewModel;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

@Component
@Scope("prototype")
public class GameJoinScene extends Scene {
    private GameJoinViewModel viewModel;


    public GameJoinScene(GameJoinViewModel viewModel) {
        super(new Region());
        this.viewModel = viewModel;
        setRoot(generateLayout());
    }

    private Region generateLayout() {
        TextField gameIdField = new TextField();
        gameIdField.textProperty().bindBidirectional(viewModel.gameId);

        Label gameIdLabel = new Label("Game ID:");

        Button joinBtn = new Button("Join");
        joinBtn.setOnAction(event -> viewModel.joinAction());

        Button createBtn = new Button("Create");
        createBtn.setOnAction(event -> viewModel.createAction());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(event -> viewModel.logoutAction());

        HBox topHBox = new HBox(logoutBtn);
        topHBox.setAlignment(Pos.CENTER_RIGHT);

        HBox spacingHBox = new HBox();

        HBox gameJoinHBox = new HBox(gameIdLabel, gameIdField, joinBtn, createBtn);
        gameJoinHBox.setAlignment(Pos.CENTER);

        Text error = new Text();
        error.textProperty().bindBidirectional(viewModel.error);

        HBox errorHBox = new HBox(error);
        errorHBox.visibleProperty().bindBidirectional(viewModel.errorVisible);

        VBox viewVBox = new VBox(
            topHBox,
            gameJoinHBox,
            errorHBox
        );
        viewVBox.setAlignment(Pos.TOP_CENTER);

        return viewVBox;
    }
}
