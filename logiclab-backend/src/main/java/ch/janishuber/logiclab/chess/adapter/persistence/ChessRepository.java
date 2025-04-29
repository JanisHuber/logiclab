package ch.janishuber.logiclab.chess.adapter.persistence;

import ch.janishuber.logiclab.chess.adapter.persistence.entity.GameEntity;
import ch.janishuber.logiclab.chess.domain.Game;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;

@RequestScoped
public class ChessRepository {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Transactional
    public int save(Game game) {
        GameEntity gameEntity = new GameEntity(game.getGameState(), game.getBoardState(), game.getCurrentTurn());
        em.persist(gameEntity);
        em.flush();
        return gameEntity.getGameId();
    }

    public Optional<Game> getGame(int gameId) {
        GameEntity entity = em.find(GameEntity.class, gameId);
        if (entity == null) {
            return Optional.empty();
        }
        Game game = Game.ofExisting(entity.getGameId(), entity.getGameState(), entity.getBoardState(), entity.getCurrentTurn());
        return Optional.of(game);
    }

    @Transactional
    public void updateGame(Game game) {
        GameEntity entity = em.find(GameEntity.class, game.getGameId());
        entity.setBoardState(game.getBoardState());
        entity.setGameState(game.getGameState());
        entity.setCurrentTurn(game.getCurrentTurn());
    }
}