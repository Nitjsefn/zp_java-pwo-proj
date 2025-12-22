package com.com.csuni.pjp.server.game;

import com.com.csuni.pjp.server.model.card.Card;

import static com.com.csuni.pjp.server.model.card.Card.Suit.*;
import static com.com.csuni.pjp.server.model.card.Card.Rank.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void cardShouldStoreSuitAndRank() {
        Card card = new Card(SPADES, ACE);

        assertEquals(SPADES, card.getSuit());
        assertEquals(ACE, card.getRank());
    }

    @Test
    void cardsWithSameRankAndSuitShouldBeEqual() {
        Card a = new Card(CLUBS, TEN);
        Card b = new Card(CLUBS, TEN);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void cardsWithDifferentSuitShouldNotBeEqual() {
        Card a = new Card(CLUBS, TEN);
        Card b = new Card(HEARTS, TEN);

        assertNotEquals(a, b);
    }
}
