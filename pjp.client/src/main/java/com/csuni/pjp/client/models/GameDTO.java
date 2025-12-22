package com.csuni.pjp.client.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;
import com.csuni.pjp.client.support.GameStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@JsonIgnoreProperties
public class GameDTO {
    @Getter
    private GameStatusEnum gameState;
    @Getter
    private String gameId;
    @Getter
    private String currentPlayer;
    @Getter
    private List<String> playerNames;
    @Getter
    private CardModel topCard;
    @Getter
    private Map<String, Integer> handSizes;
    @Getter
    private List<CardModel> myHand;
    @Getter
    private int cardsToDrawNext;
    @Getter
    private CardRankEnum chosenRank;
    @Getter
    private CardSuitEnum chosenSuit;
}
