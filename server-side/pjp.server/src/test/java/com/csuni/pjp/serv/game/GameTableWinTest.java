package com.csuni.pjp.serv.game;

import com.csuni.pjp.serv.dto.GameTableStateDTO;
import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.model.card.CardEffectManager;
import com.csuni.pjp.serv.model.card.ICardEffectManager;
import com.csuni.pjp.serv.model.deck.DeckManager;
import com.csuni.pjp.serv.model.deck.IDeckManager;
import com.csuni.pjp.serv.model.game.GameTable;
import com.csuni.pjp.serv.model.game.ITurnManager;
import com.csuni.pjp.serv.model.game.Player;
import com.csuni.pjp.serv.model.game.TurnManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.csuni.pjp.serv.model.card.Card.Suit.*;
import static com.csuni.pjp.serv.model.card.Card.Rank.*;
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
        Player winner = table.checkWinner();

        assertEquals(rick, winner);
    }

    @Test
    void playerWinsAfterPlayingLastCard() {
        rick.addCard(kingClubs);
        table.playCardForCurrentPlayer(rick, kingClubs, null, null);
        Player winner = table.checkWinner();

        assertEquals(rick, winner);
    }
}