package com.com.csuni.pjp.server.model.game;

import com.com.csuni.pjp.server.model.card.Card;
import jakarta.persistence.*;
import lombok.Getter;


import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String username;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession session;

    @Getter
    @ElementCollection
    @CollectionTable(name = "player_cards", joinColumns = @JoinColumn(name = "player_id"))
    private List<Card> hand = new ArrayList<>();

    private boolean host;

    @Setter
    @Transient
    private GameTable table;


    public Player() {}

    public Player(String username) {
        this.username = username;
    }

    public Player(String name, boolean host, GameSession session) {
        this.username = name;
        this.host = host;
        this.session = session;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        if (!hand.remove(card)) {
            throw new IllegalStateException("Player does not have this card");
        }
    }
    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public void setHost(boolean b) {

    }

}