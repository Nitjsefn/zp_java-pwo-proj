package com.com.csuni.pjp.server.model.deck;

import com.com.csuni.pjp.server.model.card.Card;

public interface IDeckManager {
    Card drawCard();
    void playCard(Card card);

    int deckSize();
    int discardPileSize();
    Card getTopCard();
}

