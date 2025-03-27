USE logiclab;

CREATE TABLE IF NOT EXISTS mastermind (
    gameId INT PRIMARY KEY AUTO_INCREMENT,
    masterMindNumber VARCHAR(4) NOT NULL,
    gameStatus VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS playerGuesses (
    guessId INT PRIMARY KEY AUTO_INCREMENT,
    guess VARCHAR(4) NOT NULL,
    correctPosition INT NOT NULL,
    correctNumber INT NOT NULL
);

CREATE TABLE IF NOT EXISTS guesstogame (
    guessId INT,
    gameId INT,
    FOREIGN KEY (guessId) REFERENCES playerGuesses(guessId) ON DELETE CASCADE,
    FOREIGN KEY (gameId) REFERENCES mastermind(gameId) ON DELETE CASCADE
);


/*DROP TABLE IF EXISTS guesstogame;
DROP TABLE IF EXISTS mastermind;
DROP TABLE IF EXISTS playerGuesses;

TRUNCATE TABLE guesstogame;
TRUNCATE TABLE mastermind;
TRUNCATE TABLE playerGuesses;*/