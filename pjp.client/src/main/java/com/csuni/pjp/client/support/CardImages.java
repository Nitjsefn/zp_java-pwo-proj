package com.csuni.pjp.client.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.csuni.pjp.client.Main;
import com.csuni.pjp.client.models.CardModel;

import javafx.scene.image.Image;
import lombok.Getter;

@Component
public class CardImages {
    @Getter
    private static final Map<CardSuitEnum, Map<CardRankEnum, Image>> cardImages = new HashMap<CardSuitEnum, Map<CardRankEnum, Image>>();

    public CardImages() {
        for(CardSuitEnum suit : CardSuitEnum.values()) {
            Map<CardRankEnum, Image> suitImages = new HashMap<CardRankEnum, Image>();
            for(CardRankEnum rank : CardRankEnum.values()) {
                String path = getCardResourcePath(suit, rank);
                try {
                    Image img = new Image(path);
                    suitImages.put(rank, img);
                }
                catch(Exception ex) {
                    System.out.println("Exception while loading: " + path);
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
            cardImages.put(suit, suitImages);
        }
    }

    public String cardToResourceName(CardModel card) {
        return cardToResourceName(card.getSuit(), card.getRank());
    }

    public String cardToResourceName(CardSuitEnum suit, CardRankEnum rank) {
        String additional = "";
        String name = "";
        switch(rank) {
            case TWO: name += "2"; break;
            case THREE: name += "3"; break;
            case FOUR: name += "4"; break;
            case FIVE: name += "5"; break;
            case SIX: name += "6"; break;
            case SEVEN: name += "7"; break;
            case EIGHT: name += "8"; break;
            case NINE: name += "9"; break;
            case TEN: name += "10"; break;
            case ACE: name += rank.name().toLowerCase(); break;
            default: name += rank.name().toLowerCase(); additional = "2"; break;
        }
        name += "_of_" + suit.name().toLowerCase() + additional + ".png";
        return name;
    }

    public String getCardResourcePath(String name) {
        return "cards/" + name;
    }

    public String getCardResourcePath(CardSuitEnum suit, CardRankEnum rank) {
        return getCardResourcePath(cardToResourceName(suit, rank));
    }

    public String getCardResourcePath(CardModel card) {
        return getCardResourcePath(cardToResourceName(card));
    }
}
