package com.com.csuni.pjp.server.dto;

import com.com.csuni.pjp.server.model.card.Card;
import com.com.csuni.pjp.server.model.card.CardEffectEnum;

public record CardDTO(Card.Suit suit, Card.Rank rank, CardEffectEnum effect) {

    public static CardDTO fromCard(Card card) {
        return new CardDTO(card.getSuit(), card.getRank(), card.getEffect());
    }
}