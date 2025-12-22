package com.com.csuni.pjp.server.dto;

import com.com.csuni.pjp.server.model.card.Card;
import com.com.csuni.pjp.server.model.game.GameStateEnum;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GameStatusResponseDTO(
        GameStateEnum gameState,
        UUID gameId,
        String currentPlayer,
        CardDTO topCard,
        int cardsToDrawNext,
        Card.Suit chosenSuit,
        Card.Rank chosenRank,
        List<String> playerNames,
        Map<String,Integer> handSizes,
        List<CardDTO> myHand
) {}

