package com.csuni.pjp.client.scenes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.viewModels.RegisterViewModel;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

@Component
@Scope("prototype")
public class RegisterScene extends Scene {
    private RegisterViewModel viewModel;


    public RegisterScene(RegisterViewModel viewModel) {
        super(new Region());
        this.viewModel = viewModel;
        setRoot(generateLayout());
    }

    private Region generateLayout() {
        TextField usernameField = new TextField();
        usernameField.textProperty().bindBidirectional(viewModel.username);

        HBox usernameHBox = 
            new HBox(
                new Label("Username:"),
                usernameField
            );
        usernameHBox.setSpacing(6);
        usernameHBox.setAlignment(Pos.CENTER_RIGHT);

        TextField emailField = new TextField();
        emailField.textProperty().bindBidirectional(viewModel.email);

        HBox emailHBox = 
            new HBox(
                new Label("Email:"),
                emailField
            );
        emailHBox.setSpacing(6);
        emailHBox.setAlignment(Pos.CENTER_RIGHT);

        PasswordField pswdField = new PasswordField();
        pswdField.textProperty().bindBidirectional(viewModel.password);

        HBox pswdHBox =
            new HBox(
                new Label("Password:"),
                pswdField
        );
        pswdHBox.setSpacing(6);
        pswdHBox.setAlignment(Pos.CENTER_RIGHT);

        Text errorText = new Text();
        errorText.textProperty().bindBidirectional(viewModel.error);

        HBox errorHBox = new HBox(errorText);
        errorHBox.setAlignment(Pos.CENTER_RIGHT);
        errorHBox.visibleProperty().bindBidirectional(viewModel.errorVisible);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnMouseClicked(event -> viewModel.cancelAction());

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(event -> viewModel.registerAction());

        HBox btnsHBox = new HBox(cancelBtn, registerBtn);
        btnsHBox.setSpacing(6);
        btnsHBox.setAlignment(Pos.CENTER_RIGHT);

        VBox credentialsVBox =
            new VBox(
                usernameHBox,
                emailHBox,
                pswdHBox,
                errorHBox,
                btnsHBox
            );
        credentialsVBox.setAlignment(Pos.CENTER);

        VBox spacingVBox = new VBox();
        VBox spacingVBox2 = new VBox();

        HBox viewVBox = new HBox(spacingVBox, credentialsVBox, spacingVBox2);
        viewVBox.setAlignment(Pos.CENTER);
        viewVBox.setHgrow(spacingVBox, Priority.SOMETIMES);
        viewVBox.setHgrow(spacingVBox2, Priority.SOMETIMES);
        viewVBox.setHgrow(credentialsVBox, Priority.NEVER);

        return viewVBox;
    }
}
