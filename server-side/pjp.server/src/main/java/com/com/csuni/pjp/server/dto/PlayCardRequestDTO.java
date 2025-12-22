package com.com.csuni.pjp.server.dto;

import com.com.csuni.pjp.server.model.card.Card;

public record PlayCardRequestDTO(
        CardDTO card,
        Card.Suit chosenSuit,
        Card.Rank chosenRank
) {}