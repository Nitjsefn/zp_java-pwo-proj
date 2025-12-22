package com.csuni.pjp.serv.dto;

import com.csuni.pjp.serv.model.card.Card;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Embeddable
public class GameTableStateDTO {

    @Embedded
    private Card topCard;

    @Enumerated(EnumType.STRING)
    private Card.Suit forcedSuit;

    @Enumerated(EnumType.STRING)
    private Card.Rank forcedRank;

    private int cardsToDrawNext;
    private int forcedRankTurnsLeft;
    private boolean skipNextPlayer;
    private int currentPlayerIndex;

    public GameTableStateDTO() {}

    public GameTableStateDTO(
            Card topCard,
            Card.Suit forcedSuit,
            Card.Rank forcedRank,
            int cardsToDrawNext,
            int forcedRankTurnsLeft,
            boolean skipNextPlayer,
            int currentPlayerIndex
    ) {
        this.topCard = topCard;
        this.forcedSuit = forcedSuit;
        this.forcedRank = forcedRank;
        this.cardsToDrawNext = cardsToDrawNext;
        this.forcedRankTurnsLeft = forcedRankTurnsLeft;
        this.skipNextPlayer = skipNextPlayer;
        this.currentPlayerIndex = currentPlayerIndex;
    }
}
