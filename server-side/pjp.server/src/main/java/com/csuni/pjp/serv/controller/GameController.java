package com.csuni.pjp.serv.controller;

import com.csuni.pjp.serv.dto.CardDTO;
import com.csuni.pjp.serv.dto.GameStatusResponseDTO;
import com.csuni.pjp.serv.dto.PlayCardRequestDTO;
import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.service.GameService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public UUID createGame(Authentication authentication) {
        String username = authentication.getName();
        return gameService.createGame(username).getId();
    }

    @PostMapping("/join/{gameId}")
    public void joinGame(
            @PathVariable UUID gameId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        gameService.joinGame(gameId, username);
    }

    @GetMapping("/status/{gameId}")
    public GameStatusResponseDTO getStatus(
            @PathVariable UUID gameId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return gameService.getGameStatus(gameId, username);
    }

    @PostMapping("/start/{gameId}")
    public void startGame(
            @PathVariable UUID gameId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        gameService.startGame(gameId, username);
    }

    @PostMapping("/play/{gameId}")
    public void playCard(
            @PathVariable UUID gameId,
            @RequestBody PlayCardRequestDTO request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        Card card = new Card(request.card().suit(), request.card().rank());
        gameService.playCard(gameId, username, card, request.chosenSuit(), request.chosenRank());
    }

    @PostMapping("/draw/{gameId}")
    public List<CardDTO> drawCard(
            @PathVariable UUID gameId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return gameService.drawCard(gameId, username);
    }
}