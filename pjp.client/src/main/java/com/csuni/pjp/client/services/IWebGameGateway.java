package com.csuni.pjp.client.services;

import com.csuni.pjp.client.models.CardModel;
import com.csuni.pjp.client.models.GameDTO;
import com.csuni.pjp.client.models.GameModel;
import com.csuni.pjp.client.models.PlaceCardDTO;

public interface IWebGameGateway {
    void startGame(String gameId);
    void place(String gameId, PlaceCardDTO card);
    void joinGame(String gameId);
    void draw(String gameId);
    String createGame();
    GameDTO gameStatus(String gameId);
}
