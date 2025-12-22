package com.csuni.pjp.client.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.GameModelDoesNotExistException;
import com.csuni.pjp.client.services.GameValidationException;
import com.csuni.pjp.client.services.IPlayCardValidator;
import com.csuni.pjp.client.services.IWebGameGateway;
import com.csuni.pjp.client.services.InvalidGameDTOFetchedException;
import com.csuni.pjp.client.support.AppUser;
import com.csuni.pjp.client.support.CardPlaceInfoEnum;
import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;
import com.csuni.pjp.client.support.GameStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class GameModel {
    @Getter
    private GameStatusEnum gameStatus;
    @Getter
    private String gameId;
    @Getter
    private String currentPlayer;
    @Getter
    private List<String> playerNames;
    @Getter
    private CardModel topCard;
    @Getter
    private Map<String, Integer> handSizes;
    @Getter
    private List<CardModel> myHand;
    @Getter
    private int cardsToDrawNext;
    @Getter
    private CardRankEnum chosenRank;
    @Getter
    private CardSuitEnum chosenSuit;
    private CardModel cardAwaiting;
    @NonNull
    private IWebGameGateway gameGateway;
    @NonNull
    private AppUser appUser;
    @NonNull
    private IPlayCardValidator playCardValidator;


    public void fetch(String id) {
        GameDTO gameDTO;
        try {
            gameDTO = gameGateway.gameStatus(id);
        }
        catch (InvalidGameDTOFetchedException ex) {
            System.out.println("Received game data is not valid");
            ex.printStackTrace();
            throw new GameModelDoesNotExistException();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new GameModelDoesNotExistException();
        }

        gameStatus = gameDTO.getGameState();
        gameId = gameDTO.getGameId();
        currentPlayer = gameDTO.getCurrentPlayer();
        playerNames = gameDTO.getPlayerNames();
        topCard = gameDTO.getTopCard();
        handSizes = gameDTO.getHandSizes();
        myHand = gameDTO.getMyHand();
        cardsToDrawNext = gameDTO.getCardsToDrawNext();
        chosenRank = gameDTO.getChosenRank();
        chosenSuit = gameDTO.getChosenSuit();
    }

    public void drawCard() {
        if(!isMyTurn()) {
            return;
        }
        try {
            gameGateway.draw(gameId);
        }
        catch (Exception ex) {
        }
    }

    public CardPlaceInfoEnum placeCard(CardModel card) {
        if(!isMyTurn() || card == null) {
            return CardPlaceInfoEnum.COMPLETE;
        }
        if(card.isChangeRank()) {
            cardAwaiting = card;
            return CardPlaceInfoEnum.RANK_MISSING;
        }
        if(card.isChangeSuit()) {
            cardAwaiting = card;
            return CardPlaceInfoEnum.SUIT_MISSING;
        }
        try {
            playCardValidator.validateMove(this, card, appUser.getUsername(), null, null);
        }
        catch (GameValidationException ex) {
            return CardPlaceInfoEnum.COMPLETE;
        }
        try {
            gameGateway.place(gameId, new PlaceCardDTO(card, null, null));
        }
        catch (Exception ex) {
        }
        return CardPlaceInfoEnum.COMPLETE;
    }

    public void startGame() {
        if(playerNames.size() < 2 || !playerNames.getFirst().equals(appUser.getUsername())) {
            return;
        }
        try {
            gameGateway.startGame(gameId);
        }
        catch (Exception ex) {
        }
    }

    public void placeCardAfterChoosingSuit(CardSuitEnum suit) {
        if(!isMyTurn()) {
            cardAwaiting = null;
            return;
        }
        try {
            playCardValidator.validateMove(this, cardAwaiting, appUser.getUsername(), suit, null);
        }
        catch (GameValidationException ex) {
            cardAwaiting = null;
            return;
        }
        PlaceCardDTO dto = new PlaceCardDTO(cardAwaiting, suit, null);
        cardAwaiting = null;
        try {
            gameGateway.place(gameId, dto);
        }
        catch (Exception ex) {
        }
    }

    public void placeCardAfterChoosingRank(CardRankEnum rank) {
        if(!isMyTurn()) {
            cardAwaiting = null;
            return;
        }
        try {
            playCardValidator.validateMove(this, cardAwaiting, appUser.getUsername(), null, rank);
        }
        catch (GameValidationException ex) {
            cardAwaiting = null;
            return;
        }
        PlaceCardDTO dto = new PlaceCardDTO(cardAwaiting, null, rank);
        cardAwaiting = null;
        try {
            gameGateway.place(gameId, dto);
        }
        catch (Exception ex) {
        }
    }

    public String create() {
        return gameGateway.createGame();
    }

    public void join(String id) {
        if(id != null && !id.isBlank()) {
            gameGateway.joinGame(id);
        }
    }

    private boolean isMyTurn() {
        if(currentPlayer.equals(appUser.getUsername())) {
            return true;
        }
        return false;
    }
}
