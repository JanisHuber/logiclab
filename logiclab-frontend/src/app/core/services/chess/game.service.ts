import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Game } from '../../models/chess/dto/game';
import { Move } from '../../models/chess/dto/move';
import { Field } from '../../models/chess/dto/field';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private apiUrl = 'http://localhost:9090/logiclab/api/chess/';

  constructor(private http: HttpClient) {}

  startNewGame(): Observable<Game> {
    return this.http.post<Game>(this.apiUrl + 'new', {});
  }

  getGameDetails(gameId: number): Observable<Game> {
    return this.http.get<Game>(this.apiUrl + gameId);
  }

  getGameState(gameId: number): Observable<Game> {
    return this.http.get<Game>(this.apiUrl + gameId + '/gameState');
  }

  makePlayerMove(gameId: number, move: Move): Observable<Game> {
    return this.http.post<Game>(this.apiUrl + gameId + '/player/move', move);
  }

  makeBotMove(gameId: number): Observable<Game> {
    return this.http.get<Game>(this.apiUrl + gameId + '/bot/move');
  }

  getPlayerPossibleMoves(gameId: number, position: string): Observable<Field[]> {
    return this.http.get<Field[]>(`${this.apiUrl}${gameId}/player/fields?position=${position}`);
  }
}