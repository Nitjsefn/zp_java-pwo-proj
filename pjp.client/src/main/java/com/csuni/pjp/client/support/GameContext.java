package com.csuni.pjp.client.support;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class GameContext {
    @Getter @Setter
    private String gameId;
}
