package com.csuni.pjp.client.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.CardModel;
import com.csuni.pjp.client.models.GameModel;
import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;

@Component
public class PlayCardValidator implements IPlayCardValidator {
    public static void validateJack(CardModel card, CardRankEnum chosenRank) {
        if (!card.isChangeRank() && chosenRank != null)
            throw new GameValidationException(
                    "NORMAL_CARD_CANNOT_HAVE_A_RANK",
                    "You cannot choose a rank while playing normal card");

        if (!card.isChangeRank()) return;

        if (chosenRank == null) {
            throw new GameValidationException(
                    "JACK_REQUIRES_RANK", "You must choose a rank when playing Jack");
        }
    }

    public static void validateAce(CardModel card, CardSuitEnum chosenSuit) {
        if (!card.isChangeSuit() && chosenSuit != null)
            throw new GameValidationException(
                    "NORMAL_CARD_CANNOT_HAVE_A_SUIT",
                    "You cannot choose a suit while playing normal card");

        if (!card.isChangeSuit()) return;

        if (chosenSuit == null) {
            throw new GameValidationException(
                    "ACE_REQUIRES_SUIT", "You must choose a suit when playing Ace");
        }
    }

    private static void validateMoveWithoutChoice(String player, CardModel card,
                                          String current, CardModel topCard,
                                          CardSuitEnum forcedSuit,
                                          CardRankEnum forcedRank,
                                          boolean hasCardsToDraw) {
        if (!player.equals(current))
            throw new GameValidationException("NOT_YOUR_TURN", "It is not your turn");

        if (topCard == null) return;

        if (card.isOverride() && !topCard.isSpecial()) return;

        if (topCard.isOverride()) return;

        if (hasCardsToDraw && !card.isDrawCard())
            throw new GameValidationException("MUST_PLAY_DRAW_CARD",
                    "Must respond with +2 or +3");

        if (forcedRank != null && card.isChangeRank()) {
            CardModel testCard = new CardModel(forcedRank, null, null);

            if (testCard.isSpecial())
                throw new GameValidationException(
                        "CANNOT_PLAY_SPECIAL_RANK",
                        "Must play the jack with non special rank");
        }
        if (forcedSuit != null && !card.isChangeSuit() &&
                card.getSuit() != forcedSuit)
            throw new GameValidationException("FORCED_SUIT",
                    "Must play the forced suit");

        if (card.isChangeRank() && !topCard.isChangeRank() && forcedRank != null)
            throw new GameValidationException("FORCED_RANK_OVERRIDE_NOT_ALLOWED",
                    "Cannot play Jack now");

        if (forcedRank != null && !card.isChangeRank()) {
            if (card.getRank() != forcedRank) {
                throw new GameValidationException("FORCED_RANK",
                        "Must play the forced rank");
            } else {
                return;
            }
        }
        if (!card.isChangeSuit() && forcedSuit == null && !card.matches(topCard))
            throw new GameValidationException("CARD_MISMATCH",
                    "Card does not match top card");
    }

    public void validateMove(GameModel gameModel, CardModel card, String player, CardSuitEnum chosenSuit, CardRankEnum chosenRank) {
        String current = gameModel.getCurrentPlayer();
        CardModel topCard = gameModel.getTopCard();
        CardSuitEnum forcedSuit = gameModel.getChosenSuit();
        CardRankEnum forcedRank = gameModel.getChosenRank();
        boolean hasCardsToDraw = gameModel.getCardsToDrawNext() > 0;
        validateMoveWithoutChoice(player, card, current, topCard, forcedSuit, forcedRank, hasCardsToDraw);

        validateJack(card, chosenRank);
        validateAce(card, chosenSuit);
    }

    public static boolean canPlayWithoutChoice(CardModel card, String player, GameModel table) {
        try {
            validateMoveWithoutChoice(player, card, table.getCurrentPlayer(),
                    table.getTopCard(), table.getChosenSuit(),
                    table.getChosenRank(),
                    table.getCardsToDrawNext() > 0);
            return true;
        } catch (GameValidationException e) {
            return false;
        }
    }

    public boolean canPlay(String player, List<CardModel> hand, GameModel gameModel) {   
        return hand.stream().anyMatch(card ->       
            PlayCardValidator.canPlayWithoutChoice(card, player, gameModel)           
        );
    }
}
