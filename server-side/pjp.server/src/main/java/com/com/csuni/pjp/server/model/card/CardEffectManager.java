package com.com.csuni.pjp.server.model.card;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CardEffectManager implements ICardEffectManager {

    @Setter
    private int cardsToDrawNext = 0;
    @Setter
    private int forcedRankTurnsLeft = 0;
    @Setter
    private Card.Suit forcedSuit = null;
    @Setter
    private Card.Rank forcedRank = null;
    @Setter
    private boolean skipNextPlayer = false;

    @Override
    public void applyEffect(Card card, Card.Suit chosenSuit, Card.Rank chosenRank, int playersCount) {
        switch(card.getEffect()) {
            case DRAW_TWO -> addCardsToDraw(2);
            case DRAW_THREE -> addCardsToDraw(3);
            case SKIP -> skipNextPlayer();
            case CHANGE_SUIT -> forceSuit(chosenSuit);
            case CHANGE_RANK -> forceRank(chosenRank, playersCount);
            case NONE -> forcedSuit = null;
        }
    }


    @Override
    public void onTurnPassed() {
        if (forcedRankTurnsLeft > 0) {
            forcedRankTurnsLeft--;
            if (forcedRankTurnsLeft == 0) forcedRank = null;
        }
        skipNextPlayer = false;
    }

    @Override
    public void resetCardsToDraw() { cardsToDrawNext = 0; }
    @Override
    public boolean hasCardsToDraw() { return cardsToDrawNext > 0; }
    @Override
    public boolean isSkipNextPlayer() { return skipNextPlayer; }

    private void addCardsToDraw(int count) { cardsToDrawNext += count; }
    private void skipNextPlayer() { skipNextPlayer = true; }
    private void forceSuit(Card.Suit suit) { forcedSuit = suit; }
    private void forceRank(Card.Rank rank, int turns) {
        forcedRank = rank;
        forcedRankTurnsLeft = turns;
    }
}