USE logiclab;

CREATE TABLE IF NOT EXISTS mastermind (
                                          gameId INT PRIMARY KEY AUTO_INCREMENT,
                                          masterMindNumber VARCHAR(4) NOT NULL,
                                          gameStatus VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS playerGuesses (
                                             guessId INT PRIMARY KEY AUTO_INCREMENT,
                                             gameId INT NOT NULL,
                                             guess VARCHAR(4) NOT NULL,
                                             correctPosition INT NOT NULL,
                                             correctNumber INT NOT NULL,
                                             FOREIGN KEY (gameId) REFERENCES mastermind(gameId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chess (
                                     gameId INT PRIMARY KEY AUTO_INCREMENT,
                                     gameState VARCHAR(255) NOT NULL,
                                     boardState TEXT NOT NULL,
                                     currentTurn VARCHAR(10) NOT NULL,
                                     againstAI BIT NOT NULL,
                                     botColor VARCHAR(255) NOT NULL,
                                     botDifficulty INT NOT NULL,
                                     moveHistory TEXT NOT NULL
);