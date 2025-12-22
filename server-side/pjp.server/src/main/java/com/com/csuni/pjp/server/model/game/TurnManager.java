package com.com.csuni.pjp.server.model.game;

import java.util.List;

public class TurnManager implements ITurnManager {

    private final List<Player> players;
    private int currentPlayerIndex;

    public TurnManager(List<Player> players) {
        this(players, 0);
    }

    public TurnManager(List<Player> players, int startIndex) {
        this.players = players;
        this.currentPlayerIndex = startIndex;
    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public void advanceTurn(int steps) {
        currentPlayerIndex = (currentPlayerIndex + steps) % players.size();
    }

    @Override
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
