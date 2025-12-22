package com.csuni.pjp.serv.service;

import com.csuni.pjp.serv.dto.CardDTO;
import com.csuni.pjp.serv.dto.GameStatusResponseDTO;
import com.csuni.pjp.serv.exception.GameValidationException;
import com.csuni.pjp.serv.model.card.Card;
import com.csuni.pjp.serv.model.game.GameSession;
import com.csuni.pjp.serv.model.game.GameTable;
import com.csuni.pjp.serv.model.game.GameTableFactory;
import com.csuni.pjp.serv.model.game.Player;
import com.csuni.pjp.serv.repository.GameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService implements IGameService {

    private final GameSessionRepository gameSessionRepository;
    private final GameTableFactory gameTableFactory;


    public GameService(
            GameSessionRepository gameSessionRepository,
            GameTableFactory gameTableFactory
    ) {
        this.gameSessionRepository = gameSessionRepository;
        this.gameTableFactory = gameTableFactory;
    }

    @Transactional
    public void startGame(UUID gameId, String username) {
        GameSession session = getSession(gameId);

        GameTable table = gameTableFactory.create(session);
        table.startGame();

        session.startGame();
        session.setTableState(table.exportState());

        gameSessionRepository.save(session);
    }


    public GameSession getSession(UUID gameId) {
        return gameSessionRepository.findById(gameId)
                .orElseThrow(() -> new GameValidationException("GAME_NOT_STARTED", "Game not started"));
    }

    @Transactional
    public void joinGame(UUID gameId, String username) {
        GameSession session = gameSessionRepository.findById(gameId)
                .orElseThrow(() -> new GameValidationException("NOT_FOUND", "Game not found"));

        if (session.getPlayers().stream().anyMatch(p -> p.getUsername().equals(username))) return;
        if (session.getPlayers().size() >= 4) throw new GameValidationException("GAME_FULL", "Game is full");

        Player newPlayer = new Player(username);
        newPlayer.setSession(session);
        session.getPlayers().add(newPlayer);
        gameSessionRepository.save(session);
    }


    @Override
    public GameSession createGame(String hostUsername) {
        GameSession session = new GameSession(hostUsername);
        return gameSessionRepository.save(session);
    }


    @Transactional(readOnly = true)
    public GameStatusResponseDTO getGameStatus(UUID gameId, String username) {
        GameSession session = getSession(gameId);
        GameTable table = gameTableFactory.create(session);

        List<Player> players = table.getPlayers();

        Player me = players.stream()
                .filter(p -> username.equals(p.getUsername()))
                .findFirst()
                .orElseThrow(() -> new GameValidationException("NOT_IN_GAME", "You are not part of this game"));

        Map<String, Integer> playerHands =
                table.getPlayers().stream()
                        .collect(Collectors.toMap(
                                Player::getUsername,
                                p -> p.getHand().size()
                        ));

        List<CardDTO> myHand = me.getHand().stream()
                .map(CardDTO::fromCard)
                .toList();

        Card top = table.getTopCard();
        CardDTO topCardDto = top != null ? CardDTO.fromCard(top) : null;

        Player currentPlayer = table.getCurrentPlayer();
        String currentPlayerName = currentPlayer != null ? currentPlayer.getUsername() : null;

        return new GameStatusResponseDTO(
                session.getStatus(),
                session.getId(),
                currentPlayerName,
                topCardDto,
                table.getCardsToDrawNext(),
                table.getForcedSuit(),
                table.getForcedRank(),
                players.stream().map(Player::getUsername).toList(),
                playerHands,
                myHand
        );
    }

    @Transactional
    public void playCard(UUID gameId, String username, Card card, Card.Suit chosenSuit, Card.Rank chosenRank) {
        GameSession session = getSession(gameId);
        GameTable table = gameTableFactory.create(session);
        table.playCardForCurrentPlayer(table.getPlayerByName(username), card, chosenSuit, chosenRank);
        session.setTableState(table.exportState());
        gameSessionRepository.save(session);
        if (table.checkWinner()!= null) session.finishGame();
    }

    @Transactional
    public List<CardDTO> drawCard(UUID gameId, String username) {
        GameSession session = getSession(gameId);
        GameTable table = gameTableFactory.create(session);
        List<Card> drawn = table.drawCardForCurrentPlayer(table.getPlayerByName(username));
        session.setTableState(table.exportState());
        gameSessionRepository.save(session);
        return drawn.stream().map(CardDTO::fromCard).toList();
    }
}