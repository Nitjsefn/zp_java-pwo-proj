package com.com.csuni.pjp.server.model.card;

import com.com.csuni.pjp.server.exception.GameValidationException;
import com.com.csuni.pjp.server.model.game.Player;

public
class CardValidator {

    public static void validateMove(
            Player player, Card card, Card topCard, Card.Suit forcedSuit,
            Card.Rank forcedRank, boolean hasCardsToDraw, Card.Suit chosenSuit, Card.Rank chosenRank){

        validateJack(card, chosenRank, forcedRank);
        validateAce(card, chosenSuit, forcedSuit);

        if (!player.hasCard(card))
            throw new GameValidationException("INVALID_MOVE", "Player does not have this card");

        if (topCard == null) return;
        if (topCard.isOverride()) return;

        if (card.isOverride() && (hasCardsToDraw || chosenRank!=card.getRank() || chosenSuit!=card.getSuit() ))
            throw new GameValidationException(
                    "CANNOT_PLAY_QUEEN_NOW", "You cannot play queen on special cards other than other queens ");
        if(card.isOverride()) return;

        if (hasCardsToDraw && card.isDrawCard()) return;

        if (hasCardsToDraw && !card.isDrawCard())
            throw new GameValidationException(
                    "MUST_PLAY_DRAW_CARD", "Must respond with +2 or +3");

        if (card.isChangeRank() && !topCard.isChangeRank() && forcedRank != null)
            throw new GameValidationException(
                    "FORCED_RANK_OVERRIDE_NOT_ALLOWED", "Cannot play Jack now");

        if (forcedRank != null && !card.isChangeRank()) {
            if (card.getRank() != forcedRank) {
                throw new GameValidationException("FORCED_RANK", "Must play the forced rank");
            } else {return;}}

        if (!card.isChangeSuit() && forcedSuit == null && !card.matches(topCard))
            throw new GameValidationException(
                    "CARD_MISMATCH", "Card does not match top card");
    }

    private static void validateJack(Card card, Card.Rank chosenRank, Card.Rank forcedRank) {

        if (!card.isChangeRank() && chosenRank != null)
            throw new GameValidationException(
                    "NORMAL_CARD_CANNOT_HAVE_A_RANK", "You cannot choose a rank while playing normal card");

        else
        if (card.isChangeRank() && chosenRank == null)
            throw new GameValidationException(
                    "JACK_REQUIRES_RANK", "You must choose a rank when playing Jack");

        if (forcedRank != null && card.isChangeRank()) {
            Card testCard = new Card(null, forcedRank);

            if (testCard.isSpecial())
                throw new GameValidationException(
                        "CANNOT_PLAY_SPECIAL_RANK", "Must play the jack with non special rank");
        }
    }
    private static void validateAce(Card card, Card.Suit chosenSuit, Card.Suit forcedSuit) {
        if (!card.isChangeSuit() && chosenSuit != null)
            throw new GameValidationException(
                    "NORMAL_CARD_CANNOT_HAVE_A_SUIT", "You cannot choose a suit while playing normal card");
        else
        if (card.isChangeSuit() && chosenSuit == null)
            throw new GameValidationException(
                    "ACE_REQUIRES_SUIT", "You must choose a suit when playing Ace");

        if (forcedSuit != null && !card.isChangeSuit() &&
                card.getSuit() != forcedSuit)
            throw new GameValidationException(
                    "FORCED_SUIT", "Must play the forced suit");
    }
}