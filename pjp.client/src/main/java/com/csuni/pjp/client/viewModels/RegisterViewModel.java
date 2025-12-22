package com.csuni.pjp.client.viewModels;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.RegisterModel;
import com.csuni.pjp.client.scenes.GameJoinScene;
import com.csuni.pjp.client.services.IWebAuthGateway;
import com.csuni.pjp.client.support.IViewManager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class RegisterViewModel {
    @NonNull
    private IViewManager viewManager;
    @NonNull
    private RegisterModel registerModel;
    @NonNull
    private ConfigurableApplicationContext appContext;
    public StringProperty username = new SimpleStringProperty();
    public StringProperty email = new SimpleStringProperty();
    public StringProperty password = new SimpleStringProperty();
    public StringProperty error = new SimpleStringProperty("");
    public BooleanProperty errorVisible = new SimpleBooleanProperty(false);


    public void registerAction() {
        registerModel.setUsername(username.get());
        registerModel.setEmail(email.get());
        registerModel.setPassword(password.get());
        String errorStr = registerModel.registerAction();
        error.set(errorStr);
        if(errorStr.length() > 0) {
            errorVisible.set(true);
        }
        else {
            errorVisible.set(false);
            viewManager.addScene(appContext.getBean(GameJoinScene.class));
        }
    }

    public void cancelAction() {
        viewManager.goBack();
    }
}
