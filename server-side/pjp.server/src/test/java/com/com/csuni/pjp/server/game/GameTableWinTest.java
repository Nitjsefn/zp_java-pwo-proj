package com.com.csuni.pjp.server.game;

import com.com.csuni.pjp.server.dto.GameTableStateDTO;
import com.com.csuni.pjp.server.model.card.Card;
import com.com.csuni.pjp.server.model.card.CardEffectManager;
import com.com.csuni.pjp.server.model.card.ICardEffectManager;
import com.com.csuni.pjp.server.model.deck.DeckManager;
import com.com.csuni.pjp.server.model.deck.IDeckManager;
import com.com.csuni.pjp.server.model.game.GameTable;
import com.com.csuni.pjp.server.model.game.ITurnManager;
import com.com.csuni.pjp.server.model.game.Player;
import com.com.csuni.pjp.server.model.game.TurnManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.com.csuni.pjp.server.model.card.Card.Suit.*;
import static com.com.csuni.pjp.server.model.card.Card.Rank.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTableWinTest {

    private GameTable table;
    private Player rick;
    private Player morty;

    Card kingClubs = new Card(CLUBS, KING);

    @BeforeEach
    void setup() {
        rick = new Player("Rick");
        morty = new Player("Morty");

        List<Player> players = new ArrayList<>();
        players.add(rick);
        players.add(morty);

        IDeckManager deckManager = new DeckManager();
        ICardEffectManager cardEffectManager = new CardEffectManager();
        ITurnManager turnManager = new TurnManager(players);

        GameTableStateDTO state = new GameTableStateDTO();

        table = new GameTable(players, state, deckManager, cardEffectManager, turnManager);
    }

    @Test
    void checkWinnerReturnsPlayerWithEmptyHand() {
        morty.addCard(new Card(SPADES, ACE));
        Player winner = table.getWinner();

        assertEquals(rick, winner);
    }

    @Test
    void playerWinsAfterPlayingLastCard() {
        rick.addCard(kingClubs);
        table.playCardForCurrentPlayer(rick, kingClubs, null, null);
        Player winner = table.getWinner();

        assertEquals(rick, winner);
    }
}