package com.csuni.pjp.serv.model.deck;

import com.csuni.pjp.serv.exception.GameValidationException;
import com.csuni.pjp.serv.model.card.Card;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Table(name = "decks")
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "deck_id")
    private List<Card> deck = new ArrayList<>();

    public Deck() {

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        if (deck.isEmpty()) {
            throw new GameValidationException("DECK_EMPTY", "Deck is empty");
        }
        return deck.removeLast();
    }

    public void addAllToBottom(List<Card> cardsToAdd) {
        for (Card card : cardsToAdd) {
            deck.addFirst(card);
        }
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public int size() {
        return deck.size();
    }

}