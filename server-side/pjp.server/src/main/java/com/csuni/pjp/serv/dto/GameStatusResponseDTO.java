package com.csuni.pjp.serv.dto;

import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.model.game.GameStateEnum;
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

