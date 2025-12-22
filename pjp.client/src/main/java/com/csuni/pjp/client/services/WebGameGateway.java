package com.csuni.pjp.client.services;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.CardModel;
import com.csuni.pjp.client.models.GameDTO;
import com.csuni.pjp.client.models.GameModel;
import com.csuni.pjp.client.models.PlaceCardDTO;
import com.csuni.pjp.client.support.RequestSender;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebGameGateway implements IWebGameGateway {
    @NonNull
    private RequestSender requestSender;
    private ObjectMapper objMapper = new ObjectMapper();
    @NonNull
    private Environment env;


    public void startGame(String gameId) {
        requestSender.request(env.getProperty("apiv1.game.start") + "/" + gameId, "POST", "");
    }

    public void place(String gameId, PlaceCardDTO card) {
        String resource = env.getProperty("apiv1.game.place") + "/" + gameId;
        String cardStr;
        try {
            cardStr = objMapper.writeValueAsString(card);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        requestSender.request(resource, "POST", cardStr);
    }

    public void joinGame(String gameId) {
        String resource = env.getProperty("apiv1.game.join") + "/" + gameId;
        requestSender.request(resource, "POST", "");
    }

    public void draw(String gameId) {
        String resource = env.getProperty("apiv1.game.draw") + "/" + gameId;
        requestSender.request(resource, "POST", "");
    }

    public String createGame() {
        String resource = env.getProperty("apiv1.game.create");
        String result = requestSender.request(resource, "POST", "");
        result = result.substring(1, result.length() - 1);
        return result;
    }

    public GameDTO gameStatus(String gameId) {
        String resource = env.getProperty("apiv1.game.status") + "/" + gameId;
        String result = requestSender.request(resource, "GET", "");
        GameDTO gameDTO;
        try {
            gameDTO = objMapper.readValue(result, GameDTO.class);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new InvalidGameDTOFetchedException();
        }
        return gameDTO;
    }
}
