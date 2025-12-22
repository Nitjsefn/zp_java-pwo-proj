package com.com.csuni.pjp.server.service;

import com.com.csuni.pjp.server.dto.CardDTO;
import com.com.csuni.pjp.server.dto.GameStatusResponseDTO;
import com.com.csuni.pjp.server.model.card.Card;
import com.com.csuni.pjp.server.model.game.GameSession;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    GameSession createGame(String hostUsername);
    GameSession getSession(UUID gameId);
    void joinGame(UUID gameId, String username);
    void startGame(UUID gameId, String username);
    GameStatusResponseDTO getGameStatus(UUID gameId, String username);
    void playCard(UUID gameId, String username, Card card, Card.Suit chosenSuit, Card.Rank chosenRank);
    List<CardDTO> drawCard(UUID gameId, String username);
}
