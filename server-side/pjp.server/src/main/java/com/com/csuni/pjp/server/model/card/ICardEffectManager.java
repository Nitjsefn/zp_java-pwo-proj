package com.com.csuni.pjp.server.model.card;


public interface ICardEffectManager {
    void applyEffect(Card card, Card.Suit chosenSuit, Card.Rank chosenRank, int playerCount);
    int getCardsToDrawNext();
    void resetCardsToDraw();
    boolean hasCardsToDraw();
    void setForcedSuit(Card.Suit suit);
    void setForcedRank(Card.Rank rank);
    void setForcedRankTurnsLeft(int turns);
    void setSkipNextPlayer(boolean skip);
    Card.Suit getForcedSuit();
    Card.Rank getForcedRank();
    int getForcedRankTurnsLeft();
    boolean isSkipNextPlayer();
    void onTurnPassed();

    void setCardsToDrawNext(int cardsToDrawNext);
}