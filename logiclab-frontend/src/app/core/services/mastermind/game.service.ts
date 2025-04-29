import { Injectable } from '@angular/core';
import { Game } from '../../models/mastermind/game';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Guess } from '../../models/mastermind/guess';

@Injectable({
  providedIn: 'root'
})

export class GameService {

  private apiUrl = 'http://localhost:9090/logiclab/api/mastermind/';

  constructor(private http: HttpClient) {}

  startNewGame(): Observable<Game> {
    return this.http.post<Game>(this.apiUrl + 'new', {});
  }

  getGameDetails(gameId: number): Observable<Game> {
    return this.http.get<Game>(this.apiUrl + gameId);
  }

  submitGuess(gameId: number, guess: string): Observable<Guess> {
    return this.http.post<Guess>(this.apiUrl + gameId + '/guesses', { guess });
  }
}
