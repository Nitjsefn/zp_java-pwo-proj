    package com.com.csuni.pjp.server.model.game;

    import com.com.csuni.pjp.server.model.card.ICardEffectManager;
    import com.com.csuni.pjp.server.model.deck.IDeckManager;
    import org.springframework.stereotype.Component;

    @Component
    public class GameTableFactory {

        private final IDeckManager deckManager;
        private final ICardEffectManager cardEffectManager;

        public GameTableFactory(
                IDeckManager deckManager,
                ICardEffectManager cardEffectManager
        ) {
            this.deckManager = deckManager;
            this.cardEffectManager = cardEffectManager;
        }

        public GameTable create(GameSession session) {
            return new GameTable(
                    session.getPlayers(),
                    session.getTableState(),
                    deckManager,
                    cardEffectManager,
                    new TurnManager(session.getPlayers())
            );
        }
    }