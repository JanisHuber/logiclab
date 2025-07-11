use logiclab;

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