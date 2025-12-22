package com.csuni.pjp.client.models;

import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlaceCardDTO {
    public CardModel card;
    public CardSuitEnum chosenSuit;
    public CardRankEnum chosenRank;
}
