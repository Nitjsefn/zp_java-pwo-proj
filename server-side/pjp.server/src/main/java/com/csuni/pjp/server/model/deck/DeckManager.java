package com.csuni.pjp.serv.model.deck;

import com.csuni.pjp.serv.exception.GameValidationException;
import com.csuni.pjp.serv.model.card.Card;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class DeckManager implements IDeckManager {



    private Deck deck = new Deck();
    private List<Card> discardPile = new ArrayList<>();
    @Setter
    private Card topCard = null;

    @Override
    public Card drawCard() {
        if (deck.isEmpty()) {
            refillDeckFromDiscard();
        }
        return deck.draw();
    }

    @Override
    public void playCard(Card card) {
        topCard = card;
        discardPile.add(card);
    }

    public void refillDeckFromDiscard() {
        if (discardPile.isEmpty()) {
            throw new GameValidationException("NO_CARDS_LEFT", "No cards left to draw");//nowa regula soon
        }
        Card last = discardPile.removeLast();
        List<Card> rest = new ArrayList<>(discardPile);
        discardPile.clear();
        discardPile.add(last);

        deck.addAllToBottom(rest);
        deck.shuffle();
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
    @Override
    public int size() {
        return deck.size();
    }
}