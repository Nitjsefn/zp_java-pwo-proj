package com.csuni.pjp.serv.game;

import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.csuni.pjp.serv.model.card.Card.Suit.*;
import static com.csuni.pjp.serv.model.card.Card.Rank.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private Card aceSpades;

    @BeforeEach
    void setup() {
        player = new Player("Rick");
        aceSpades = new Card(SPADES, ACE);
    }

    @Test
    void playerShouldStartWithEmptyHand() {
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void addCardShouldAddCardToHand() {
        player.addCard(aceSpades);

        assertEquals(1, player.getHand().size());
        assertTrue(player.hasCard(aceSpades));
    }

    @Test
    void removeCardShouldRemoveCardFromHand() {
        player.addCard(aceSpades);
        player.removeCard(aceSpades);

        assertFalse(player.hasCard(aceSpades));
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void removingCardNotInHandShouldThrowException() {
        assertThrows(IllegalStateException.class,
                () -> player.removeCard(aceSpades));
    }
}
