package com.com.csuni.pjp.server.game;

import com.com.csuni.pjp.server.dto.GameTableStateDTO;
import com.com.csuni.pjp.server.exception.ValidationException;
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


class GameTableTwoPlayersTest {
    private GameTable table;
    private Player rick;
    private Player morty;

    private final Card aceSpades = new Card(SPADES, ACE);
    private final Card aceHearts = new Card(HEARTS, ACE);
    private final Card twoDiamonds = new Card(DIAMONDS, TWO);
    private final Card twoClubs = new Card(CLUBS, TWO);
    private final Card kingDiamonds = new Card(DIAMONDS, KING);
    private final Card threeHearts = new Card(HEARTS, THREE);
    private final Card threeClubs = new Card(CLUBS, THREE);
    private final Card fourClubs = new Card(CLUBS, FOUR);
    private final Card queenHearts = new Card(HEARTS, QUEEN);
    private final Card queenClubs = new Card(CLUBS, QUEEN);
    private final Card queenSpades = new Card(SPADES, QUEEN);
    private final Card kingSpades = new Card(SPADES, KING);

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

        GameTable realTable = new GameTable(players, state, deckManager, cardEffectManager, turnManager);

        table = spy(realTable);

        when(table.getWinner()).thenReturn(null);
    }


    @Test
    void currentPlayerCanPlayCard() {
        rick.addCard(aceSpades);

        table.playCardForCurrentPlayer(rick, aceSpades, HEARTS,null);

        assertEquals(morty, table.getCurrentPlayer());
        assertEquals(aceSpades, table.getTopCard());
        assertEquals(0, rick.getHand().size());
    }

    @Test
    void playerCannotPlayOutOfTurn() {
        assertThrows(ValidationException.class,
                () -> table.playCardForCurrentPlayer(morty, aceSpades, null,null));
    }

    @Test
    void playerCannotPlayCardNotInHand() {
        assertThrows(ValidationException.class,
                () -> table.playCardForCurrentPlayer(rick, aceSpades, null,null));
    }

    @Test
    void playerCannotPlayCardThatDoesNotMatchTopCard() {
        rick.addCard(aceSpades);
        table.playCardForCurrentPlayer(rick, aceSpades, HEARTS,null);

        Card invalid = twoDiamonds;
        morty.addCard(invalid);

        assertThrows(ValidationException.class,
                () -> table.playCardForCurrentPlayer(morty, invalid, null,null));
    }

    @Test
    void playerMustDrawIfNoValidCard() {
        rick.addCard(aceSpades);
        table.playCardForCurrentPlayer(rick, aceSpades, HEARTS,null);

        assertEquals(0, morty.getHand().size());
        morty.getHand().clear();
        table.drawCardForCurrentPlayer(morty);
        assertEquals(1, morty.getHand().size());
        assertEquals(rick, table.getCurrentPlayer());
    }

    @Test
    void twoForcesNextPlayerToDrawTwoCards() {
        rick.addCard(twoDiamonds);
        morty.addCard(kingDiamonds);
        table.playCardForCurrentPlayer(rick, twoDiamonds, null,null);

        assertThrows(ValidationException.class, () -> table.playCardForCurrentPlayer(morty, kingDiamonds, null,null));

        table.drawCardForCurrentPlayer(morty);
        assertEquals(3, morty.getHand().size());
        assertEquals(rick, table.getCurrentPlayer());
    }




    @Test
    void twoOnTwoStackToFour() {
        rick.addCard(twoDiamonds);
        morty.addCard(twoClubs);
        table.playCardForCurrentPlayer(rick, twoDiamonds, null,null);

        table.playCardForCurrentPlayer(morty, twoClubs, null,null);
        table.drawCardForCurrentPlayer(rick);
        assertEquals(4, rick.getHand().size());
        assertEquals(morty, table.getCurrentPlayer());
    }
    @Test
    void threeOnThreeStackToSix() {
        rick.addCard(threeHearts);
        morty.addCard(threeClubs);
        table.playCardForCurrentPlayer(rick, threeHearts, null,null);

        table.playCardForCurrentPlayer(morty, threeClubs, null,null);
        table.drawCardForCurrentPlayer(rick);
        assertEquals(6, rick.getHand().size());
        assertEquals(morty, table.getCurrentPlayer());
    }


    @Test
    void twoOnThreeStackToFive() {
        rick.addCard(threeHearts);
        morty.addCard(twoClubs);
        table.playCardForCurrentPlayer(rick, threeHearts, null,null);

        table.playCardForCurrentPlayer(morty, twoClubs, null,null);
        table.drawCardForCurrentPlayer(rick);
        assertEquals(5, rick.getHand().size());
        assertEquals(morty, table.getCurrentPlayer());
    }

    @Test
    void threeOnTwoStackToFive() {
        rick.addCard(twoClubs);
        morty.addCard(threeHearts);
        table.playCardForCurrentPlayer(rick, twoClubs, null,null);

        table.playCardForCurrentPlayer(morty, threeHearts, null,null);
        table.drawCardForCurrentPlayer(rick);
        assertEquals(5, rick.getHand().size());
        assertEquals(morty, table.getCurrentPlayer());
    }


    @Test
    void queenOnEverything_EverythingOnQueen() {
        morty.addCard(queenClubs);
        morty.addCard(queenHearts);
        rick.addCard(kingDiamonds);
        rick.addCard(kingSpades);

        table.playCardForCurrentPlayer(rick, kingSpades, null,null);

        table.playCardForCurrentPlayer(morty, queenClubs, null,null);

        table.playCardForCurrentPlayer(rick, kingDiamonds, null,null);
        table.playCardForCurrentPlayer(morty, queenHearts, null,null);
    }

    @Test
    void queenOnQueen() {


        rick.addCard(queenClubs);
        morty.addCard(queenHearts);

        table.playCardForCurrentPlayer(rick, queenClubs, null,null);
        table.playCardForCurrentPlayer(morty, queenHearts, null,null);
    }

    @Test
    void queenOnEverything_EverythingOnQueenButQueenNotOnSpecialCards() {
        morty.addCard(queenHearts);
        morty.addCard(kingDiamonds);
        morty.addCard(queenClubs);

        rick.addCard(queenSpades);
        rick.addCard(queenClubs);
        rick.addCard(twoDiamonds);

        table.playCardForCurrentPlayer(rick, queenClubs, null,null);
        table.playCardForCurrentPlayer(morty, kingDiamonds, null,null);
        table.playCardForCurrentPlayer(rick, queenSpades, null,null);
        table.playCardForCurrentPlayer(morty, queenClubs, null,null);
        table.playCardForCurrentPlayer(rick, twoDiamonds, null,null);
        assertThrows(ValidationException.class, () -> table.playCardForCurrentPlayer(morty, queenHearts, null,null));
    }


    @Test
    void threeForcesNextPlayerToDrawThreeCards() {
        rick.addCard(threeHearts);
        table.playCardForCurrentPlayer(rick, threeHearts, null,null);

        table.drawCardForCurrentPlayer(morty);
        assertEquals(3, morty.getHand().size());
        assertEquals(rick, table.getCurrentPlayer());
    }

    @Test
    void fourSkipsOnlyOneTurn() {
        rick.addCard(fourClubs);
        table.playCardForCurrentPlayer(rick, fourClubs, null,null);

        assertEquals(rick, table.getCurrentPlayer());
    }

    @Test
    void normalCardDoesNotAllowChoosingSuit() {
        rick.addCard(kingDiamonds);


        assertThrows(ValidationException.class,
                () ->table.playCardForCurrentPlayer(rick, kingDiamonds, SPADES, null));
        assertNull(table.getForcedSuit());
    }

    @Test
    void aceAllowsChoosingSuit() {
        morty.addCard(kingSpades);
        morty.addCard(kingDiamonds);
        morty.addCard(aceHearts);


        rick.addCard(aceSpades);
        rick.addCard(kingSpades);

        table.playCardForCurrentPlayer(rick, aceSpades, DIAMONDS, null);
        assertEquals(DIAMONDS, table.getForcedSuit());

        table.playCardForCurrentPlayer(morty, kingDiamonds, null, null);

        assertNull(table.getForcedSuit());
    }

    @Test
    void playerCannotDrawCardDuringSomeonesElseTurn() {
        assertEquals(rick, table.getCurrentPlayer());
        assertThrows(ValidationException.class,
                () ->table.drawCardForCurrentPlayer(morty));
    }
}
