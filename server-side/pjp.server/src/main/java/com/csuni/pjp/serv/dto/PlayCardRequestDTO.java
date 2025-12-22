package com.csuni.pjp.serv.dto;

import com.csuni.pjp.serv.model.card.Card;

public record PlayCardRequestDTO(
        CardDTO card,
        Card.Suit chosenSuit,
        Card.Rank chosenRank
) {}