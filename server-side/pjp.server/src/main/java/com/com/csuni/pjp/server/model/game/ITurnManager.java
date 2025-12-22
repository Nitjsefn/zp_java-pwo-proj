package com.com.csuni.pjp.server.model.game;

public interface ITurnManager {
    Player getCurrentPlayer();
    void advanceTurn(int steps);
    int getCurrentPlayerIndex();
}