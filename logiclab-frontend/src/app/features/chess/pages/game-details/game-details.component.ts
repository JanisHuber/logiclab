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
  possibleMoves: { col: string, row: number }[] = [];
  gameOver: boolean = false;
  winner: string | undefined;

  ngOnInit() {
    this.updateBoard();
  }

  onFigureClicked(figure: ChessFigure) {
    if (!this.gameId) return;
    //todo Client side validation
    if (figure.figureColor !== this.currentTurn && this.selectedFigure) {
      this.gameService.makePlayerMove(this.gameId, {source: this.selectedFigure.position, target: figure.position}).subscribe((response) => {
        this.updateBoard();
        this.getBotMove();
      });
      return;
    }
    if (this.selectedFigure === figure) {
      this.selectedFigure = null;
      this.possibleMoves = [];
      return;
    }
    if (figure.figureColor !== this.currentTurn) {
      return;
    }
    this.selectedFigure = figure;
    this.possibleMoves = [];
    this.gameService.getPlayerPossibleMoves(this.gameId, figure.position).subscribe((moves) => {
      if (moves.length > 0) {
        moves.forEach((move) => {
          this.possibleMoves.push({ col: move.column, row: move.row });
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

  onMoveMade(move: { col: number, row: number }) {
    const gameId = this.gameId;
    if (!gameId) return;
    
    let source = this.selectedFigure?.position;
    let target = boardToApiPosition(move.col, move.row);
    if (!source || (target.convertedRow + target.convertedColumn) === source) {
      return;
    }
    if (!this.possibleMoves.some(move => move.col === target.convertedColumn && move.row === target.convertedRow)) {
      return;
    }
    this.possibleMoves = [];
    this.selectedFigure = null;
    console.log("Source: ", source, "Target: ", target.convertedRow + target.convertedColumn);
    this.gameService.makePlayerMove(gameId, {source: source, target: target.convertedColumn + target.convertedRow}).subscribe((response) => {
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
