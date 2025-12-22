package com.com.csuni.pjp.server.repository;

import com.com.csuni.pjp.server.model.game.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public interface GameSessionRepository
        extends JpaRepository<GameSession, UUID> {
}