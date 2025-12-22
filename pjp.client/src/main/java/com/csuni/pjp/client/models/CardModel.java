package com.csuni.pjp.client.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.csuni.pjp.client.support.CardRankEnum;
import com.csuni.pjp.client.support.CardSuitEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class CardModel {
    @Getter @Setter
    private CardRankEnum rank;
    @Getter @Setter
    private CardSuitEnum suit;
    @JsonIgnore
    @Getter @Setter
    private String effect;


    @JsonIgnore
    public boolean isChangeRank() {
        if(rank == CardRankEnum.JACK) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isChangeSuit() {
        if(rank == CardRankEnum.ACE) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isOverride() {
        if(rank == CardRankEnum.QUEEN) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isSpecial() {
        List<CardRankEnum> normalRanks = Arrays.asList(new CardRankEnum[]{CardRankEnum.FIVE, CardRankEnum.SIX,
            CardRankEnum.SEVEN, CardRankEnum.EIGHT, CardRankEnum.NINE,
            CardRankEnum.TEN, CardRankEnum.KING
        });

        if(normalRanks.contains(rank)) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    public boolean isDrawCard() {
        if(rank == CardRankEnum.TWO || rank == CardRankEnum.THREE) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean matches(CardModel card) {
        if(rank == card.rank || suit == card.suit) {
            return true;
        }
        return false;
    }
}
