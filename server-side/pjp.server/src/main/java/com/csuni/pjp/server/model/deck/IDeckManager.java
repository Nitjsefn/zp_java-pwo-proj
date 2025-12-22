package com.csuni.pjp.serv.model.deck;

import com.csuni.pjp.serv.model.card.Card;

public interface IDeckManager {
    Card drawCard();
    void playCard(Card card);
    int size();

    Card getTopCard();
}

