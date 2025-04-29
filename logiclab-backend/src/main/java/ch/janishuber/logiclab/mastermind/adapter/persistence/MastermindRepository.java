package ch.janishuber.logiclab.mastermind.adapter.persistence;

import ch.janishuber.logiclab.mastermind.adapter.persistence.entity.MastermindEntity;
import ch.janishuber.logiclab.mastermind.domain.Game;
import ch.janishuber.logiclab.mastermind.domain.Guess;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@RequestScoped
public class MastermindRepository {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Transactional
    public int save(Game game) {
        MastermindEntity mastermindEntity = new MastermindEntity(game.getMasterMindNumber(), game.getGameStatus());
        em.persist(mastermindEntity);
        em.flush();
        return mastermindEntity.getGameId();
    }

    public Optional<Game> getGame(int gameId, Guess[] guesses) {
        MastermindEntity entity = em.find(MastermindEntity.class, gameId);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(Game.ofExisting(entity.getGameId(), entity.getMastermindNumber(), entity.getGameStatus(), guesses));
    }

    @Transactional
    public void updateGame(Game game) {
        MastermindEntity entity = em.find(MastermindEntity.class, game.getId());
        entity.setGameStatus(game.getGameStatus());
    }
}
