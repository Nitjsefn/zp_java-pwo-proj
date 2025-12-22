package com.csuni.pjp.serv.model.game;

import com.csuni.pjp.serv.dto.GameTableStateDTO;
import com.csuni.pjp.serv.exception.GameValidationException;
import com.csuni.pjp.serv.model.card.Card;

import com.csuni.pjp.serv.model.card.CardValidator;
import com.csuni.pjp.serv.model.card.ICardEffectManager;
import com.csuni.pjp.serv.model.deck.IDeckManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTable {

    private final List<Player> players;
    @Getter
    private final IDeckManager deckManager;
    private final ICardEffectManager cardEffectManager;
    private final ITurnManager turnManager;

    public GameTable(List<Player> players,
                     GameTableStateDTO state,
                     IDeckManager deckManager,
                     ICardEffectManager cardEffectManager,
                     ITurnManager turnManager) {
        this.players = players != null ? players : new ArrayList<>();
        this.deckManager = deckManager;
        this.cardEffectManager = cardEffectManager;
        this.turnManager = turnManager;

        int startIndex = (state != null) ? state.getCurrentPlayerIndex() : 0;
        this.turnManager.advanceTurn(startIndex);

        if (state != null) {
            if (state.getTopCard() != null) deckManager.playCard(state.getTopCard());
            cardEffectManager.setForcedSuit(state.getForcedSuit());
            cardEffectManager.setForcedRank(state.getForcedRank());
            cardEffectManager.setCardsToDrawNext(state.getCardsToDrawNext());
            cardEffectManager.setForcedRankTurnsLeft(state.getForcedRankTurnsLeft());
            cardEffectManager.setSkipNextPlayer(state.isSkipNextPlayer());
        }
    }

public GameTableStateDTO exportState() {
        return new GameTableStateDTO(
                deckManager.getTopCard(),
                cardEffectManager.getForcedSuit(),
                cardEffectManager.getForcedRank(),
                cardEffectManager.getCardsToDrawNext(),
                cardEffectManager.getForcedRankTurnsLeft(),
                cardEffectManager.isSkipNextPlayer(),
                turnManager.getCurrentPlayerIndex()
        );
    }

    public void startGame() {
        if (players.size() < 2 || players.size() > 4)
            throw new GameValidationException("INVALID_PLAYER_COUNT", "The game requires 2 to 4 players.");

        for (Player player : players) {
            drawCards(player, 5);
        }

        Card firstCard = drawFirstNonSpecialCard();
        deckManager.playCard(firstCard);
    }

    Card drawFirstNonSpecialCard() {
        Card firstCard;
        do {
            firstCard = deckManager.drawCard();
        } while (firstCard.isSpecial());
        return firstCard;
    }

    public void playCardForCurrentPlayer(Player player, Card card, Card.Suit chosenSuit, Card.Rank chosenRank) {
        checkPlayerTurn(player);

        CardValidator.validateMove(player, card, deckManager.getTopCard(), cardEffectManager.getForcedSuit(),
                cardEffectManager.getForcedRank(), cardEffectManager.hasCardsToDraw(), chosenSuit, chosenRank);

        deckManager.playCard(card);
        player.removeCard(card);

        cardEffectManager.applyEffect(card, chosenSuit, chosenRank, players.size());

        int step = checkWinner() != null ? 0 : (card.isSkip() ? 2 : 1);
        turnManager.advanceTurn(step);

        cardEffectManager.onTurnPassed();

    }

    public List<Card> drawCardForCurrentPlayer(Player player) {
        checkPlayerTurn(player);
        int cardsToDraw = cardEffectManager.hasCardsToDraw() ? cardEffectManager.getCardsToDrawNext() : 1;

        List<Card> drawnCards = drawCards(player, cardsToDraw);
        cardEffectManager.resetCardsToDraw();

        turnManager.advanceTurn(1);
        cardEffectManager.onTurnPassed();
        return drawnCards;
    }

    public Player checkWinner() {
        return players.stream()
                .filter(p -> p.getHand().isEmpty())
                .findFirst()
                .orElse(null);
    }

    public Player getPlayerByName(String username) {
        return players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() ->
                        new GameValidationException("NOT_IN_GAME", "You are not part of this game"));
    }
    public Player getHost() {
        return players.getFirst();
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }
    public Card getTopCard() {
        return deckManager.getTopCard();
    }
    public Card.Suit getForcedSuit() {
        return cardEffectManager.getForcedSuit();
    }
    public Card.Rank getForcedRank() {
        return cardEffectManager.getForcedRank();
    }
    public int getCardsToDrawNext() {
        return cardEffectManager.getCardsToDrawNext();
    }

    private List<Card> drawCards(Player player, int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Card card = deckManager.drawCard();
            player.addCard(card);
            drawnCards.add(card);
        }
        return drawnCards;
    }

    private void checkPlayerTurn(Player player){
        if (!player.equals(getCurrentPlayer())) throw new GameValidationException("NOT_YOUR_TURN", "It is not your turn");
    }

    // For tests methods
    public int getDeckSize() {
        return deckManager.size();
    }
    public int getForcedRankTurnsLeft() {
        return cardEffectManager.getForcedRankTurnsLeft();
    }

}