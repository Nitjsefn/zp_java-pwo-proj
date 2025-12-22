package com.csuni.pjp.client.services;

import com.csuni.pjp.client.models.CardModel;
import com.csuni.pjp.client.models.GameModel;
import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;

public interface IPlayCardValidator {
    void validateMove(GameModel gameModel, CardModel card, String player, CardSuitEnum chosenSuit, CardRankEnum chosenRank);
}
