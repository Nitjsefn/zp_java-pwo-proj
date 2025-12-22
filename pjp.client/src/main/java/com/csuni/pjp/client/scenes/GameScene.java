package com.csuni.pjp.client.scenes;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.CardModel;
import com.csuni.pjp.client.support.CardImages;
import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;
import com.csuni.pjp.client.support.GameContext;
import com.csuni.pjp.client.viewModels.GameViewModel;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

@Component
@Scope("prototype")
public class GameScene extends Scene {
    private GameViewModel viewModel;
    private GameContext gameContext;
    private CardImages cardImages;


    public GameScene(GameViewModel viewModel, GameContext gameContext, CardImages cardImages) {
        super(new Region());
        this.gameContext = gameContext;
        this.cardImages = cardImages;
        this.viewModel = viewModel;
        this.viewModel.startTimer();
        setRoot(generateLayout());
    }

    public Region generateLayout() {
        Button quitBtn = new Button("Quit");
        quitBtn.setOnAction(event -> viewModel.quitAction());

        Button startBtn = new Button("Start");
        startBtn.visibleProperty().bindBidirectional(viewModel.startBtnVisible);
        startBtn.setOnAction(event -> viewModel.startAction());

        Button drawBtn = new Button("Draw");
        drawBtn.visibleProperty().bindBidirectional(viewModel.drawBtnVisible);
        drawBtn.setOnAction(event -> viewModel.drawAction());
        drawBtn.textProperty().bindBidirectional(viewModel.drawBtnText);

        Text gameIdCopiedText = new Text("✓");
        gameIdCopiedText.visibleProperty().bindBidirectional(viewModel.copiedVisible);

        Text gameIdText = new Text("📋 Game ID: " + gameContext.getGameId());
        gameIdText.setStyle("-fx-font-size: 2em; -fx-font-weight: bold;");
        gameIdText.setOnMouseClicked(event -> viewModel.gameIdClicked());

        HBox topHBox = new HBox(gameIdCopiedText, gameIdText, startBtn, quitBtn);
        topHBox.setAlignment(Pos.CENTER_RIGHT);
        topHBox.setSpacing(10);

        Label enemy1 = new Label();
        enemy1.textProperty().bindBidirectional(viewModel.enemy1Text);
        enemy1.visibleProperty().bindBidirectional(viewModel.enemy1Visible);
        enemy1.textFillProperty().bind(viewModel.enemy1LabelColor);

        Label enemy2 = new Label();
        enemy2.textProperty().bindBidirectional(viewModel.enemy2Text);
        enemy2.visibleProperty().bindBidirectional(viewModel.enemy2Visible);
        enemy2.textFillProperty().bind(viewModel.enemy2LabelColor);

        Label enemy3 = new Label();
        enemy3.textProperty().bindBidirectional(viewModel.enemy3Text);
        enemy3.visibleProperty().bindBidirectional(viewModel.enemy3Visible);
        enemy3.textFillProperty().bind(viewModel.enemy3LabelColor);

        ImageView topCard = new ImageView();
        topCard.imageProperty().bindBidirectional(viewModel.topCardImage);
        topCard.setFitHeight(300);
        topCard.setPreserveRatio(true);

        Text chosenRankText = new Text();
        chosenRankText.visibleProperty().bindBidirectional(viewModel.chosenRankVisible);
        chosenRankText.textProperty().bindBidirectional(viewModel.chosenRankText);
        chosenRankText.setStyle("-fx-font-size: 4em; -fx-font-weight: bold;");

        ImageView chosenSuitImage = new ImageView();
        chosenSuitImage.visibleProperty().bindBidirectional(viewModel.chosenSuitVisible);
        chosenSuitImage.imageProperty().bindBidirectional(viewModel.chosenSuitImage);
        chosenSuitImage.setFitHeight(75);
        chosenSuitImage.setPreserveRatio(true);

        HBox handHBox = new HBox();
        viewModel.handChangedListener = () -> handListChangedListener(viewModel, handHBox);
        handHBox.setAlignment(Pos.BOTTOM_CENTER);
        handHBox.setMaxHeight(300);
        handHBox.setPadding(new Insets(20));
        handHBox.setSpacing(10);
        handHBox.backgroundProperty().bindBidirectional(viewModel.playerCardsBackground);
        ScrollPane hand = new ScrollPane(handHBox);
        hand.setFitToHeight(true);
        hand.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        hand.setMaxHeight(350);
        hand.setPadding(new Insets(0));

        HBox firstLineHBox = new HBox(enemy2);
        firstLineHBox.setAlignment(Pos.CENTER);

        HBox seconLineHBox = new HBox(enemy1, drawBtn, topCard, chosenSuitImage, chosenRankText, enemy3);
        seconLineHBox.setAlignment(Pos.CENTER);
        seconLineHBox.setPadding(new Insets(0));

        HBox thirdLineHBox = new HBox(hand);
        thirdLineHBox.setAlignment(Pos.CENTER);

        VBox viewVBox = new VBox(topHBox, firstLineHBox, seconLineHBox, thirdLineHBox);

        Text winnerText = new Text();
        winnerText.textProperty().bindBidirectional(viewModel.winnerText);
        winnerText.setStyle("-fx-font-size: 2em;");

        Button quitBtn2 = new Button("Quit");
        quitBtn2.setOnAction(event -> viewModel.quitAction());

        VBox winnerViewVBox = new VBox(winnerText, quitBtn2);
        winnerViewVBox.visibleProperty().bindBidirectional(viewModel.winnerScreenVisible);
        winnerViewVBox.setAlignment(Pos.CENTER);
        winnerViewVBox.setStyle("-fx-background-color: #cececeef");

        HBox cardRanksHBox = new HBox();
        populateChooseRankPane(cardRanksHBox);
        cardRanksHBox.setSpacing(10);
        cardRanksHBox.setAlignment(Pos.CENTER);
        ScrollPane cardRanksView = new ScrollPane(cardRanksHBox);
        cardRanksView.setFitToHeight(true);
        cardRanksView.visibleProperty().bindBidirectional(viewModel.chooseRankScreenVisible);
        cardRanksView.setStyle("-fx-background-color: #cececeef");

        HBox cardSuitsHBox = new HBox();
        populateChooseSuitPane(cardSuitsHBox);
        cardSuitsHBox.setSpacing(10);
        cardSuitsHBox.setAlignment(Pos.CENTER);
        ScrollPane cardSuitsView = new ScrollPane(cardSuitsHBox);
        cardSuitsView.setFitToHeight(true);
        cardSuitsView.visibleProperty().bindBidirectional(viewModel.chooseSuitScreenVisible);
        cardSuitsView.setStyle("-fx-background-color: #cececeef");

        VBox chooseRankVBox = new VBox(cardRanksView);
        chooseRankVBox.visibleProperty().bindBidirectional(viewModel.chooseRankScreenVisible);
        chooseRankVBox.setAlignment(Pos.CENTER);

        VBox chooseSuitVBox = new VBox(cardSuitsView);
        chooseSuitVBox.visibleProperty().bindBidirectional(viewModel.chooseSuitScreenVisible);
        chooseSuitVBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(viewVBox, winnerViewVBox, chooseRankVBox, chooseSuitVBox);

        return stackPane;
    }

    private void handListChangedListener(GameViewModel vm, HBox hbox) {
        hbox.getChildren().clear();
        for(CardModel card : vm.cards) {
            Image img = cardImages.getCardImages().get(card.getSuit()).get(card.getRank());
            ImageView imageView = new ImageView(img);
            imageView.setOnMouseClicked((ev) -> vm.placeAction(card));
            imageView.setFitHeight(250);
            imageView.setPreserveRatio(true);
            imageView.setManaged(true);
            hbox.getChildren().add(imageView);
        }
    }

    private void populateChooseSuitPane(Pane pane) {
        pane.getChildren().clear();
        for(CardSuitEnum suit : viewModel.cardSuitEnums) {
            ImageView imageView = new ImageView(viewModel.suitResourceFromSuit(suit));
            imageView.setOnMouseClicked((ev) -> viewModel.chooseSuitAction(suit));
            imageView.setFitHeight(75);
            imageView.setPreserveRatio(true);
            pane.getChildren().add(imageView);
        }
    }

    private void populateChooseRankPane(Pane pane) {
        pane.getChildren().clear();
        for(CardRankEnum rank : viewModel.cardRankEnums) {
            Text text = new Text(viewModel.rankToString(rank));
            text.setOnMouseClicked((ev) -> viewModel.chooseRankAction(rank));
            text.setStyle("-fx-font-size: 4em; -fx-font-weight: bold;");
            pane.getChildren().add(text);
        }
    }
}
