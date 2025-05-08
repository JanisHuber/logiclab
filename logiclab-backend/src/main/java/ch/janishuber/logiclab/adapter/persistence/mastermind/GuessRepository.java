package ch.janishuber.logiclab.adapter.persistence.mastermind;

import ch.janishuber.logiclab.adapter.persistence.mastermind.entity.GuessEntity;
import ch.janishuber.logiclab.domain.mastermind.Guess;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class GuessRepository {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Transactional
    public void save(int gameId, Guess guess) {
        GuessEntity entity = new GuessEntity(gameId, guess.guess(), guess.correctPositions(), guess.correctNumbers());
        em.persist(entity);
    }

    public List<Guess> getGuesses(int gameId) {
        List<Guess> guesses = new ArrayList<>();

        TypedQuery<GuessEntity> query = em.createQuery(
                "SELECT g FROM GuessEntity g WHERE g.gameId = :gameId", GuessEntity.class);
        query.setParameter("gameId", gameId);
        List<GuessEntity> guessEntities = query.getResultList();

        for (GuessEntity guessEntity : guessEntities) {
            guesses.add(new Guess(guessEntity.getGuess(), guessEntity.getCorrectPosition(), guessEntity.getCorrectNumber()));
        }
        return guesses;
    }
}
