import { Guess } from "./guess";

export interface Game {
    gameId: number;
    gameState: string;
    guesses: Guess[];
}
