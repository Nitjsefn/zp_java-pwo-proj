package com.csuni.pjp.serv.dto;

import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.model.card.CardEffectEnum;

public record CardDTO(Card.Suit suit, Card.Rank rank, CardEffectEnum effect) {

    public static CardDTO fromCard(Card card) {
        return new CardDTO(card.getSuit(), card.getRank(), card.getEffect());
    }
}