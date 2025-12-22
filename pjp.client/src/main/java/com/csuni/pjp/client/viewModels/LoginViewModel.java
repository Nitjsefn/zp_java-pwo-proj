package com.csuni.pjp.client.viewModels;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.LoginModel;
import com.csuni.pjp.client.scenes.GameJoinScene;
import com.csuni.pjp.client.scenes.RegisterScene;
import com.csuni.pjp.client.support.IViewManager;
import com.csuni.pjp.client.support.ViewManager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Component
@Scope("prototype")
public class LoginViewModel {
    @NonNull
    private IViewManager viewManager;
    @NonNull
    private ConfigurableApplicationContext appContext;
    @NonNull
    private LoginModel loginModel;
    public StringProperty login = new SimpleStringProperty("");
    public StringProperty password = new SimpleStringProperty("");
    public StringProperty error = new SimpleStringProperty("");
    public BooleanProperty errorVisible = new SimpleBooleanProperty(false);


    public void loginAction() {
        System.out.println("Login Button Clicked");
        System.out.println("login: " + login.get());
        loginModel.setLogin(login.get());
        loginModel.setPassword(password.get());
        String errorStr = loginModel.loginAction();
        error.set(errorStr);
        if(errorStr.length() > 0) {
            errorVisible.set(true);
        }
        else {
            errorVisible.set(false);
            viewManager.addScene(appContext.getBean(GameJoinScene.class));
        }
    }

    public void registerAction() {
        viewManager.addScene(appContext.getBean(RegisterScene.class));
    }
}
