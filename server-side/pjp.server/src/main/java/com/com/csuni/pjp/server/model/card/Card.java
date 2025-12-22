package com.com.csuni.pjp.server.model.card;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Embeddable
public class Card {

    @Getter
    @Enumerated(EnumType.STRING)
    private Card.Suit suit;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name="`rank`")
    private Card.Rank rank;

    protected Card() {}
    public Card(Card.Suit suit, Card.Rank rank) { this.suit = suit; this.rank = rank; }


    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN,
        EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return suit == card.suit && rank == card.rank;
    }
    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
    public boolean matches(Card other) {
        return suit == other.suit || rank == other.rank;
    }
    public CardEffectEnum getEffect() {
        return switch (rank) {
            case TWO -> CardEffectEnum.DRAW_TWO;
            case THREE -> CardEffectEnum.DRAW_THREE;
            case FOUR -> CardEffectEnum.SKIP;
            case JACK -> CardEffectEnum.CHANGE_RANK;
            case ACE -> CardEffectEnum.CHANGE_SUIT;
            case QUEEN -> CardEffectEnum.OVERRIDE;
            default -> CardEffectEnum.NONE;
        };
    }
    public boolean isSpecial() {
        return getEffect() != CardEffectEnum.NONE;
    }
    public boolean isDrawCard() {
        return getEffect() == CardEffectEnum.DRAW_THREE || getEffect() == CardEffectEnum.DRAW_TWO;
    }
    public boolean isSkip() {
        return getEffect() == CardEffectEnum.SKIP;
    }
    public boolean isChangeRank() {
        return getEffect() == CardEffectEnum.CHANGE_RANK;
    }
    public boolean isChangeSuit() {
        return getEffect() == CardEffectEnum.CHANGE_SUIT;
    }
    public boolean isOverride() {
        return getEffect() == CardEffectEnum.OVERRIDE;
    }
}