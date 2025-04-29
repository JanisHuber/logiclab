import { Component } from '@angular/core';
import { ChessboardComponent } from '../components/chessboard/chessboard.component';
import { CommonModule } from '@angular/common';
import { GameService } from '../../../../core/services/chess/game.service';
import { boardToApiPosition, jsonToChessBoard } from '../../../../shared/shared/chess.helpers';
import { ChessFigure } from '../../../../core/models/chess/logic/chessfigure';
import { ActivatedRoute, Router } from '@angular/router';
import { GameOverviewComponent } from '../game-overview/game-overview.component';

@Component({
  selector: 'app-game-details',
  standalone: true,
  imports: [CommonModule, ChessboardComponent, GameOverviewComponent],
  templateUrl: './game-details.component.html',
  styleUrls: ['./game-details.component.css']
})

export class GameDetailsComponent {
  constructor(private gameService: GameService, private route: ActivatedRoute, private router: Router) {}

  gameId: number | undefined;
  chessBoard: (ChessFigure | null)[][] = [];
  currentTurn: string = 'white';

  selectedFigure: ChessFigure | null = null;
  possibleMoves: { row: string, column: number }[] = [];
  gameOver: boolean = false;
  winner: string | undefined;

  ngOnInit() {
    this.updateBoard();
  }

  onFigureClicked(figure: ChessFigure) {
    if (!this.gameId) return;
    //todo Client side validation

    if (this.selectedFigure === figure) {
      this.selectedFigure = null;
      this.possibleMoves = [];
      return;
    }
    this.selectedFigure = figure;
    this.possibleMoves = [];
    this.gameService.getPlayerPossibleMoves(this.gameId, figure.position).subscribe((moves) => {
      if (moves.length > 0) {
        moves.forEach((move) => {
          this.possibleMoves.push({ row: move.row, column: move.column });
        });
      } else {
        this.possibleMoves = [];
      }
    });
  }

  updateBoard() {
    this.gameId = Number(this.route.snapshot.paramMap.get('gameId'));
    this.gameService.getGameDetails(this.gameId).subscribe((game) => {
      this.chessBoard = jsonToChessBoard(game.boardState);
      this.currentTurn = game.currentTurn;
    });
    this.gameService.getGameState(this.gameId).subscribe((gameState) => {
      if (gameState.gameState === 'STALEMATE' || gameState.gameState === 'CHECKMATE') {
        this.gameOver = true;
        if (gameState.gameState === 'STALEMATE') {
          this.winner = "STALEMATE";
        } else {
          this.winner = (gameState.currentTurn === 'BLACK') ? "White" : "Black";
        }
      }
    });
  }

  onMoveMade(move: { row: number, column: number }) {
    const gameId = this.gameId;
    if (!gameId) return;
    
    let source = this.selectedFigure?.position;
    let target = boardToApiPosition(move.column, move.row);
    if (!source || (target.convertedRow + target.convertedColumn) === source) {
      return;
    } 
    this.possibleMoves = [];
    this.selectedFigure = null;
    this.gameService.makePlayerMove(gameId, {source: source, target: target.convertedRow + target.convertedColumn}).subscribe((response) => {
      console.log(response);
      this.updateBoard();
      this.getBotMove();
    });
  }

  getBotMove() {
    if (!this.gameId) return;
    this.gameService.makeBotMove(this.gameId).subscribe((response) => {
      this.updateBoard();
    });
  }

  onStartedNewGame() {
    this.router.navigate(['/chess/new']);
  }
}
