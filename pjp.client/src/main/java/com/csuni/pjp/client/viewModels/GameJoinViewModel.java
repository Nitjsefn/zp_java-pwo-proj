package com.csuni.pjp.client.viewModels;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.GameModel;
import com.csuni.pjp.client.models.LoginModel;
import com.csuni.pjp.client.scenes.GameScene;
import com.csuni.pjp.client.services.IWebAuthGateway;
import com.csuni.pjp.client.support.AppUser;
import com.csuni.pjp.client.support.GameContext;
import com.csuni.pjp.client.support.IViewManager;
import com.csuni.pjp.client.support.UnsuccessfullWebResponseException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class GameJoinViewModel {
    @NonNull
    private IViewManager viewManager;
    @NonNull
    private AppUser appUser;
    @NonNull
    private LoginModel loginModel;
    @NonNull
    private GameModel gameModel;
    @NonNull
    private GameContext gameContext;
    @NonNull
    private ConfigurableApplicationContext appContext;

    public StringProperty gameId = new SimpleStringProperty();
    public StringProperty error = new SimpleStringProperty();
    public BooleanProperty errorVisible = new SimpleBooleanProperty(false);

    
    public void joinAction() {
        checkForLoggedIn();
        errorVisible.set(false);
        try {
            gameModel.join(gameId.get());
        }
        catch (UnsuccessfullWebResponseException ex) {
            errorVisible.set(true);
            error.set("Cannot join to given game");
            return;
        }
        gameContext.setGameId(gameId.get());
        viewManager.addScene(appContext.getBean(GameScene.class));
    }

    public void createAction() {
        checkForLoggedIn();
        errorVisible.set(false);
        String id = gameModel.create();
        gameContext.setGameId(id);
        viewManager.addScene(appContext.getBean(GameScene.class));
    }

    public void logoutAction() {
        errorVisible.set(false);
        loginModel.logout();
        checkForLoggedIn();
    }

    public void checkForLoggedIn() {
        if(!appUser.isLoggedIn()) {
            viewManager.resetToFirst();
        }
    }
}
