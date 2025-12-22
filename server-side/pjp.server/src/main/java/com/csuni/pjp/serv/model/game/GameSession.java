package com.csuni.pjp.serv.model.game;
import com.csuni.pjp.serv.dto.GameTableStateDTO;
import com.csuni.pjp.serv.exception.GameValidationException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "game_sessions")
public class GameSession {

    @Getter
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, nullable = false, updatable = false)
    private UUID id;

    @Getter
    @Enumerated(EnumType.STRING)
    private GameStateEnum status;

    @Getter
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @Transient
    private GameTable table;

    @Setter
    @Getter
    @Embedded
    private GameTableStateDTO tableState;

    protected GameSession() {}

    public GameSession(String hostUsername) {
        this.id = UUID.randomUUID();
        this.status = GameStateEnum.WAITING_FOR_PLAYERS;

        Player host = new Player(hostUsername);
        host.setHost(true);
        host.setSession(this);
        players.add(host);
    }

    public void startGame() {
        if (status != GameStateEnum.WAITING_FOR_PLAYERS)
            throw new GameValidationException("GAME_ALREADY_STARTED", "Game already started");

        status = GameStateEnum.IN_PROGRESS;
    }
    public void finishGame() {
        status = GameStateEnum.FINISHED;
    }
}