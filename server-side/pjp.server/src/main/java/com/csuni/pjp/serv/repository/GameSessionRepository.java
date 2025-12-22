package com.csuni.pjp.serv.repository;

import com.csuni.pjp.serv.model.game.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public interface GameSessionRepository
        extends JpaRepository<GameSession, UUID> {
}

