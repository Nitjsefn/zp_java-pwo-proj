package com.csuni.pjp.client.scenes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.viewModels.LoginViewModel;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

@Component
@Scope("prototype")
public class LoginScene extends Scene {
    private LoginViewModel viewModel;


    public LoginScene(LoginViewModel viewModel) {
        super(new Region());
        this.viewModel = viewModel;
        setRoot(generateLayout());
        init();
    }
    
    private void init() {
        Paint paint = Paint.valueOf("#212121");
        setFill(paint);
    }

    private Region generateLayout() {
        TextField loginField = new TextField();
        loginField.textProperty().bindBidirectional(viewModel.login);

        HBox loginHBox = 
            new HBox(
                new Label("Login:"),
                loginField
            );
        loginHBox.setSpacing(6);
        loginHBox.setAlignment(Pos.CENTER_RIGHT);

        PasswordField pswdField = new PasswordField();
        pswdField.textProperty().bindBidirectional(viewModel.password);

        HBox pswdHBox =
            new HBox(
                new Label("Password:"),
                pswdField
        );
        pswdHBox.setSpacing(6);
        pswdHBox.setAlignment(Pos.CENTER_RIGHT);

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(event -> viewModel.loginAction());
        
        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(event -> viewModel.registerAction());

        HBox btnsHBox = new HBox(registerBtn, loginBtn);
        btnsHBox.setSpacing(6);
        btnsHBox.setAlignment(Pos.CENTER_RIGHT);

        Text errorText = new Text();
        errorText.textProperty().bindBidirectional(viewModel.error);

        HBox errorHBox = new HBox(errorText);
        errorHBox.setAlignment(Pos.CENTER_RIGHT);
        errorHBox.visibleProperty().bindBidirectional(viewModel.errorVisible);

        VBox credentialsVBox =
            new VBox(
                loginHBox,
                pswdHBox,
                errorHBox,
                btnsHBox
            );
        credentialsVBox.setAlignment(Pos.CENTER);

        VBox spacingVBox = new VBox();
        VBox spacingVBox2 = new VBox();

        HBox viewHBox = new HBox(spacingVBox, credentialsVBox, spacingVBox2);
        viewHBox.setHgrow(spacingVBox, Priority.SOMETIMES);
        viewHBox.setHgrow(spacingVBox2, Priority.SOMETIMES);
        viewHBox.setHgrow(credentialsVBox, Priority.NEVER);

        return viewHBox;
    }
}
