package com.csuni.pjp.serv.model.game;

public interface ITurnManager {
    Player getCurrentPlayer();
    void advanceTurn(int steps);
    int getCurrentPlayerIndex();
}