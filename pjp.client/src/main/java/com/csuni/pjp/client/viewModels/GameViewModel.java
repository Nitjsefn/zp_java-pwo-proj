package com.csuni.pjp.client.viewModels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.CardModel;
import com.csuni.pjp.client.models.GameModel;
import com.csuni.pjp.client.support.AppUser;
import com.csuni.pjp.client.support.CardImages;
import com.csuni.pjp.client.support.CardPlaceInfoEnum;
import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;
import com.csuni.pjp.client.support.GameContext;
import com.csuni.pjp.client.support.IViewManager;
import com.csuni.pjp.client.support.TimersRegistry;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class GameViewModel {
    @NonNull
    private IViewManager viewManager;
    private final Color ACTIVE_COLOR = Color.ORANGERED;
    private final Color INACTIVE_COLOR = Color.DIMGRAY;
    @NonNull
    private GameModel gameModel;
    @NonNull
    private AppUser appUser;
    @NonNull
    private GameContext gameContext;
    private Timer timer = new Timer();
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    @NonNull
    private TimersRegistry timersRegistry;
    @NonNull
    public CardImages cardImages;

    public BooleanProperty enemy1Visible = new SimpleBooleanProperty(false);
    public BooleanProperty enemy2Visible = new SimpleBooleanProperty(false);
    public BooleanProperty enemy3Visible = new SimpleBooleanProperty(false);
    public StringProperty enemy1Text = new SimpleStringProperty();
    public StringProperty enemy2Text = new SimpleStringProperty();
    public StringProperty enemy3Text = new SimpleStringProperty();
    public ObjectProperty<Color> enemy1LabelColor = new SimpleObjectProperty<Color>();
    public ObjectProperty<Color> enemy2LabelColor = new SimpleObjectProperty<Color>();
    public ObjectProperty<Color> enemy3LabelColor = new SimpleObjectProperty<Color>();
    public ObjectProperty<Color> playerCardsColor = new SimpleObjectProperty<Color>();
    public ObservableList<CardModel> cards = FXCollections.observableArrayList();
    public BooleanProperty chosenSuitVisible = new SimpleBooleanProperty(false);
    public BooleanProperty chosenRankVisible = new SimpleBooleanProperty(false);
    public BooleanProperty drawBtnVisible = new SimpleBooleanProperty(false);
    public BooleanProperty startBtnVisible = new SimpleBooleanProperty(false);
    public BooleanProperty winnerScreenVisible = new SimpleBooleanProperty(false);
    public StringProperty winnerText = new SimpleStringProperty();
    public StringProperty chosenRankText = new SimpleStringProperty();
    public ObjectProperty<Image> chosenSuitImage = new SimpleObjectProperty<Image>();
    public BooleanProperty chooseSuitScreenVisible = new SimpleBooleanProperty(false);
    public BooleanProperty chooseRankScreenVisible = new SimpleBooleanProperty(false);
    private final CardRankEnum[] limitedCardRanks = {
        CardRankEnum.FIVE, CardRankEnum.SIX, CardRankEnum.SEVEN,
        CardRankEnum.EIGHT, CardRankEnum.NINE, CardRankEnum.TEN, CardRankEnum.KING
    };
    public final ObservableList<CardRankEnum> cardRankEnums = FXCollections.observableArrayList(limitedCardRanks);
    public final ObservableList<CardSuitEnum> cardSuitEnums = FXCollections.observableArrayList(CardSuitEnum.values());
    public BooleanProperty copiedVisible = new SimpleBooleanProperty(false);
    public ObjectProperty<Image> topCardImage = new SimpleObjectProperty<>();
    public Runnable handChangedListener;
    public StringProperty drawBtnText = new SimpleStringProperty("Draw");
    public ObjectProperty<Background> playerCardsBackground = new SimpleObjectProperty<>();


    public void startTimer() {
        timersRegistry.register(timer);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkGameStatus();
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("Checking for game updates...");
            }
        }, 0, 1000);
    }

    public void checkGameStatus() {
        checkForLoggedIn();
        gameModel.fetch(gameContext.getGameId());
        Platform.runLater(() -> updateProperties());
    }

    public void quitAction() {
        timer.cancel();
        viewManager.goBack();
    }

    public void drawAction() {
        checkForLoggedIn();
        gameModel.drawCard();
        checkGameStatus();
    }

    public void placeAction(CardModel card) {
        checkForLoggedIn();
        CardPlaceInfoEnum result = gameModel.placeCard(card);
        switch(result) {
            case COMPLETE: updateProperties(); break;
            case RANK_MISSING: chooseRankScreenVisible.set(true); break;
            case SUIT_MISSING: chooseSuitScreenVisible.set(true); break;
            default: break;
        }
    }

    public void startAction() {
        checkForLoggedIn();
        gameModel.startGame();
        startBtnVisible.set(false);
        checkGameStatus();
    }

    public void chooseSuitAction(CardSuitEnum suit) {
        checkForLoggedIn();
        chooseSuitScreenVisible.set(false);
        gameModel.placeCardAfterChoosingSuit(suit);
        checkGameStatus();
    }
    
    public void chooseRankAction(CardRankEnum rank) {
        checkForLoggedIn();
        chooseRankScreenVisible.set(false);
        gameModel.placeCardAfterChoosingRank(rank);
        checkGameStatus();
    }

    public void gameIdClicked() {
        ClipboardContent content = new ClipboardContent();
        content.putString(gameContext.getGameId());
        clipboard.setContent(content);
        copiedVisible.set(true);
    }

    private void checkForLoggedIn() {
        if(!appUser.isLoggedIn()) {
            timer.cancel();
            viewManager.resetToFirst();
        }
    }

    private void updateProperties() {
        List<String> names = gameModel.getPlayerNames();
        List<String> relativeNames = new ArrayList<String>(names);
        relativateList(relativeNames, appUser.getUsername());
        relativeNames.removeFirst();

        setEnemyProperties(relativeNames);

        switch(gameModel.getGameStatus()) {
            case WAITING_FOR_PLAYERS: notStartedUpdateProperties(relativeNames); break;
            case IN_PROGRESS: inProgressUpdateProperties(relativeNames); break;
            case FINISHED: finishedUpdateProperties(relativeNames); break;
            default: break;
        }
    }

    private void notStartedUpdateProperties(List<String> relativeNames) {
        if(appUser.getUsername().equals(gameModel.getPlayerNames().getFirst())) {
            startBtnVisible.set(true);
        }
    }

    private void inProgressUpdateProperties(List<String> relativeNames) {
        startBtnVisible.set(false);
        updateCards();
        updateTopCard();
        setTurnIndicator(relativeNames);
        updateChosenIndicators();
        if(appUser.getUsername().equals(gameModel.getCurrentPlayer())) {
            drawBtnVisible.set(true);
            if(gameModel.getCardsToDrawNext() > 0) {
                drawBtnText.set("Draw +" + gameModel.getCardsToDrawNext());
            }
            else {
                drawBtnText.set("Draw");
            }
        }
        else {
            drawBtnVisible.set(false);
        }
    }

    private void finishedUpdateProperties(List<String> relativeNames) {
        startBtnVisible.set(false);
        updateCards();
        updateTopCard();
        setTurnIndicator(relativeNames);
        updateChosenIndicators();
        drawBtnVisible.set(false);
        winnerText.set("Won: " + gameModel.getCurrentPlayer());
        winnerScreenVisible.set(true);
    }

    public void relativateList(List<String> l, String to) {
        for(int i = 0; i < l.size(); i++) {
            if(l.getFirst().equals(to)) {
                return;
            }
            l.add(l.removeFirst());
        }
    }

    private void setEnemyProperties(List<String> relativeNames) {
        Map<String, Integer> handSizes = gameModel.getHandSizes();

        if(relativeNames.size() == 0) {
            enemy1Visible.set(false);
            enemy2Visible.set(false);
            enemy3Visible.set(false);
        }
        else if(relativeNames.size() == 1) {
            enemy1Visible.set(false);
            enemy2Visible.set(true);
            enemy3Visible.set(false);

            enemy2Text.set(relativeNames.get(0)
                + "\nCards: " + handSizes.get(relativeNames.get(0)));
        }
        else if(relativeNames.size() == 2) {
            enemy1Visible.set(true);
            enemy2Visible.set(true);
            enemy3Visible.set(false);

            enemy1Text.set(relativeNames.get(0)
                + "\nCards: " + handSizes.get(relativeNames.get(0)));
            enemy2Text.set(relativeNames.get(1)
                + "\nCards: " + handSizes.get(relativeNames.get(1)));
        }
        else if(relativeNames.size() == 3) {
            enemy1Visible.set(true);
            enemy2Visible.set(true);
            enemy3Visible.set(true);

            enemy1Text.set(relativeNames.get(0)
                + "\nCards: " + handSizes.get(relativeNames.get(0)));
            enemy2Text.set(relativeNames.get(1)
                + "\nCards: " + handSizes.get(relativeNames.get(1)));
            enemy3Text.set(relativeNames.get(2)
                + "\nCards: " + handSizes.get(relativeNames.get(2)));
        }
    }

    private void setTurnIndicator(List<String> relativeNames) {
        List<String> list = new ArrayList<String>(relativeNames);
        switch(list.size()) {
            case 0: list.add(""); list.add(""); list.add(""); break;
            case 1: list.addFirst(""); list.addLast(""); break;
            case 2: list.addLast(""); break;
        }
        int i = 0;
        for(i = 0; i < list.size(); i++) {
            if(list.get(i).equals(gameModel.getCurrentPlayer())) {
                break;
            }
        }

        switch(i) {
            case 0:
                enemy1LabelColor.set(ACTIVE_COLOR);
                enemy2LabelColor.set(INACTIVE_COLOR);
                enemy3LabelColor.set(INACTIVE_COLOR);
                playerCardsColor.set(INACTIVE_COLOR);
                playerCardsBackground.set(new Background(new BackgroundFill(INACTIVE_COLOR, null, null)));
                break;
            case 1:
                enemy1LabelColor.set(INACTIVE_COLOR);
                enemy2LabelColor.set(ACTIVE_COLOR);
                enemy3LabelColor.set(INACTIVE_COLOR);
                playerCardsColor.set(INACTIVE_COLOR);
                playerCardsBackground.set(new Background(new BackgroundFill(INACTIVE_COLOR, null, null)));
                break;
            case 2:
                enemy1LabelColor.set(INACTIVE_COLOR);
                enemy2LabelColor.set(INACTIVE_COLOR);
                enemy3LabelColor.set(ACTIVE_COLOR);
                playerCardsColor.set(INACTIVE_COLOR);
                playerCardsBackground.set(new Background(new BackgroundFill(INACTIVE_COLOR, null, null)));
                break;
            case 3:
                enemy1LabelColor.set(INACTIVE_COLOR);
                enemy2LabelColor.set(INACTIVE_COLOR);
                enemy3LabelColor.set(INACTIVE_COLOR);
                playerCardsColor.set(ACTIVE_COLOR);
                playerCardsBackground.set(new Background(new BackgroundFill(ACTIVE_COLOR, null, null)));
                break;
        }
    }

    private void updateCards() {
        cards.clear();
        cards.addAll(gameModel.getMyHand());
        handChangedListener.run();
    }

    private void updateTopCard() {
        if(gameModel.getTopCard() == null) {
            return;
        }
        CardModel card = gameModel.getTopCard();
        topCardImage.set(cardImages.getCardImages().get(card.getSuit()).get(card.getRank()));
    }

    private void updateChosenIndicators() {
        chosenRankVisible.set(gameModel.getChosenRank() != null);
        chosenSuitVisible.set(gameModel.getChosenSuit() != null);
        if(gameModel.getChosenRank() != null) {
            chosenRankText.set(rankToString(gameModel.getChosenRank()));
        }
        if(gameModel.getChosenSuit() != null) {
            chosenSuitImage.set(new Image(suitResourceFromSuit(gameModel.getChosenSuit())));
        }
    }

    public String suitResourceFromSuit(CardSuitEnum suit) {
        return "suits/" + suit.name().toLowerCase() + ".png";
    }

    public String rankToString(CardRankEnum rank) {
        String rankStr = "";
        switch(rank) {
            case TWO: rankStr = "2"; break;
            case THREE: rankStr = "3"; break;
            case FOUR: rankStr = "4"; break;
            case FIVE: rankStr = "5"; break;
            case SIX: rankStr = "6"; break;
            case SEVEN: rankStr = "7"; break;
            case EIGHT: rankStr = "8"; break;
            case NINE: rankStr = "9"; break;
            case TEN: rankStr = "10"; break;
            default: rankStr = rank.name().substring(0, 1); break;
        }
        return rankStr;
    }
}
