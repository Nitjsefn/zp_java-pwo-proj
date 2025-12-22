package com.com.csuni.pjp.server.game;

import com.com.csuni.pjp.server.dto.GameTableStateDTO;
import com.com.csuni.pjp.server.exception.GameValidationException;
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
import static org.mockito.Mockito.*;


class GameTableThreePlayersTest {

    private GameTable table;
    private Player rick;
    private Player morty;
    private Player summer;

    Card jackDiamonds = new Card(DIAMONDS, JACK);
    Card tenSpades = new Card(SPADES, TEN);
    Card tenClubs = new Card(CLUBS, TEN);
    Card tenDiamonds = new Card(DIAMONDS, TEN);
    Card kingClubs = new Card(CLUBS, KING);
    Card nineClubs = new Card(CLUBS, NINE);
    Card jackSpades = new Card(SPADES, JACK);


    @BeforeEach
    void setup() {
        rick = new Player("Rick");
        morty = new Player("Morty");
        summer = new Player("Summer");

        List<Player> players = new ArrayList<>();
        players.add(rick);
        players.add(morty);
        players.add(summer);

        IDeckManager deckManager = new DeckManager();
        ICardEffectManager cardEffectManager = new CardEffectManager();
        ITurnManager turnManager = new TurnManager(players);

        GameTableStateDTO state = new GameTableStateDTO();

        GameTable realTable = new GameTable(players, state, deckManager, cardEffectManager, turnManager);

        table = spy(realTable);

        when(table.getWinner()).thenReturn(null);
    }

    @Test
    void startGameDealsCardsAndSetsFirstCard() {
        assertEquals(52, table.getDeckSize());

        table.startGame();

        assertEquals(5, rick.getHand().size());
        assertEquals(5, morty.getHand().size());
        assertEquals(5, summer.getHand().size());

        Card first = table.getTopCard();
        assertNotNull(first);
        assertFalse(first.isDrawCard());
        assertFalse(first.isSkip());
        assertFalse(first.isChangeSuit());
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

    @Test
    void jackTest() {
        rick.addCard(tenDiamonds);
        morty.addCard(jackDiamonds);
        summer.addCard(tenSpades);

        rick.addCard(tenClubs);
        morty.addCard(kingClubs);

        table.playCardForCurrentPlayer(rick, tenDiamonds, null, null);

        table.playCardForCurrentPlayer(morty, jackDiamonds, null, TEN);
        assertEquals(2, table.getForcedRankTurnsLeft());

        table.playCardForCurrentPlayer(summer, tenSpades, null, null);
        assertEquals(1, table.getForcedRankTurnsLeft());

        table.playCardForCurrentPlayer(rick, tenClubs, null, null);

        table.playCardForCurrentPlayer(morty, kingClubs, null, null);
    }

    @Test
    void jackOnJackTest() {
        rick.addCard(jackDiamonds);
        morty.addCard(jackSpades);
        morty.addCard(tenSpades);

        summer.addCard(nineClubs);

        rick.addCard(kingClubs);

        table.playCardForCurrentPlayer(rick, jackDiamonds, null, TEN);
        assertEquals(2, table.getForcedRankTurnsLeft());

        table.playCardForCurrentPlayer(morty, jackSpades, null, NINE);
        assertEquals(2, table.getForcedRankTurnsLeft());

        table.playCardForCurrentPlayer(summer, nineClubs, null, null);
    }

    @Test
    void jackOnNormalCardTest() {
        morty.addCard(jackSpades);
        rick.addCard(tenSpades);

        table.playCardForCurrentPlayer(rick, tenSpades, null, null);
        table.playCardForCurrentPlayer(morty, jackSpades, null, TEN);
        assertEquals(2, table.getForcedRankTurnsLeft());
    }

    @Test
    void jackCannotOrderSpecialCard() {

        rick.addCard(jackSpades);

        table.playCardForCurrentPlayer(rick, jackSpades, null, FIVE);
        assertEquals(FIVE, table.getForcedRank());
    }

    @Test
    void normalCardCannotOrderCardSuit() {
        Card kingHeart = new Card(HEARTS, KING);
        rick.addCard(kingHeart);

        assertThrows(GameValidationException.class,
                () -> table.playCardForCurrentPlayer(rick, kingHeart, null, FIVE));

        assertNull(table.getForcedRank());
    }

    @Test
    void jackOnJackTest2() {
        rick.addCard(jackDiamonds);
        morty.addCard(jackSpades);
        morty.addCard(tenSpades);
        summer.addCard(jackSpades);
        rick.addCard(kingClubs);

        table.playCardForCurrentPlayer(rick, jackDiamonds, null, TEN);
        assertEquals(2, table.getForcedRankTurnsLeft());

        table.playCardForCurrentPlayer(morty, tenSpades, null, null);
        assertEquals(1, table.getForcedRankTurnsLeft());

        assertThrows(GameValidationException.class,
                () -> table.playCardForCurrentPlayer(summer, jackSpades, null, NINE));
    }

    @Test
    void jackWithDraw() {
        rick.addCard(jackSpades);
        rick.addCard(tenSpades);

        table.playCardForCurrentPlayer(rick, jackSpades, null, TEN);
        assertEquals(2, table.getForcedRankTurnsLeft());

        table.drawCardForCurrentPlayer(morty);
        assertEquals(1, table.getForcedRankTurnsLeft());

        table.drawCardForCurrentPlayer(summer);
        assertEquals(0, table.getForcedRankTurnsLeft());

        assertThrows(GameValidationException.class,
                () ->table.playCardForCurrentPlayer(rick, tenSpades, null, TEN));
    }

}