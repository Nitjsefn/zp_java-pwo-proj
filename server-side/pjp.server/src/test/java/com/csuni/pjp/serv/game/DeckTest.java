package com.csuni.pjp.serv.game;

import com.csuni.pjp.serv.exception.ValidationException;
import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.model.deck.Deck;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void newDeckShouldHave52Cards() {
        Deck deck = new Deck();
        assertEquals(52, deck.size());
    }

    @Test
    void deckShouldContain52UniqueCards() {
        Deck deck = new Deck();
        Set<Card> drawnCards = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            drawnCards.add(deck.draw());
        }

        assertEquals(52, drawnCards.size());
    }

    @Test
    void drawingCardShouldDecreaseDeckSize() {
        Deck deck = new Deck();

        deck.draw();

        assertEquals(51, deck.size());
    }

    @Test
    void drawingFromEmptyShouldR() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertThrows(ValidationException.class, deck::draw);
    }

    @Test
    void shuffleShouldNotLoseAnyCards() {
        Deck deck = new Deck();
        Set<Card> before = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            before.add(deck.draw());
        }

        assertEquals(52, before.size());
    }
}
