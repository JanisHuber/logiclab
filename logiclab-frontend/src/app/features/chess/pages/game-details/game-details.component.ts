import { Component } from '@angular/core';
import { ChessboardComponent } from '../components/chessboard/chessboard.component';
import { CommonModule } from '@angular/common';
import { GameService } from '../../../../core/services/chess/game.service';
import { boardToApiPosition, jsonToChessBoard } from '../../../../shared/shared/chess.helpers';
import { ChessFigure } from '../../../../core/models/chess/logic/chessfigure';
import { ActivatedRoute, Router } from '@angular/router';
import { GameOverviewComponent } from '../game-overview/game-overview.component';
import { Game } from '../../../../core/models/chess/dto/game';

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
  pieceValue: number = 0;
  
  ngOnInit() {
    this.updateBoard();
  }

  fieldClicked(targetPosition: { col: number, row: number }) {
    if (!this.gameId) return;
    const figure = this.chessBoard[targetPosition.row][targetPosition.col];

    if (this.selectedFigure) {
      if (figure && this.isFigureAlreadySelected(figure)) {
        this.unselectFigure();
        return;
      }
      if (figure && this.isFigureSelectable(figure)) {
        this.selectFigure(figure, this.gameId);
        return;
      }
      const target = boardToApiPosition(targetPosition.col, targetPosition.row);
      if (this.isMoveInPossibleMoves(target)) {
        this.makeMove(this.gameId, this.selectedFigure.position, target.convertedColumn + target.convertedRow);
      }
      this.unselectFigure();
    } else if (figure && this.isFigureSelectable(figure)) {
      this.selectFigure(figure, this.gameId);
    }
  }

  updateBoard() {
    this.gameId = Number(this.route.snapshot.paramMap.get('gameId'));
    this.gameService.getGameDetails(this.gameId).subscribe((game) => {
      this.chessBoard = jsonToChessBoard(game.boardState);
      this.currentTurn = game.currentTurn;
      this.updateGamestate(game);
    });
  }

  private updateGamestate(game: Game) {
    if (game.gameState === 'STALEMATE' || game.gameState === 'CHECKMATE') {
      this.gameOver = true;
      if (game.gameState === 'STALEMATE') {
        this.winner = "STALEMATE";
      } else {
        this.winner = (game.currentTurn === 'BLACK') ? "White" : "Black";
      }
    }
  }

  isMoveInPossibleMoves(target: { convertedColumn: string; convertedRow: number; }) {
    return this.possibleMoves.some(move => move.col === target.convertedColumn && move.row === target.convertedRow);
  }

  private makeMove(gameId: number, source: string, target: string) {
    this.gameService.makePlayerMove(gameId, { source: source, target: target, promotingFigureClassName: "Queen" }).subscribe((response) => {
      this.unselectFigure();
      this.updateBoard();
      this.getBotMove();
    });
  }

  private unselectFigure() {
    this.selectedFigure = null;
    this.possibleMoves = [];
  }

  private selectFigure(figure: ChessFigure, gameId: number) {
    this.selectedFigure = figure;
    this.possibleMoves = [];
    this.gameService.getPlayerPossibleMoves(gameId, figure.position).subscribe((moves) => {
      if (moves.length > 0) {
        moves.forEach((move) => {
          this.possibleMoves.push({ col: move.column, row: move.row });
        });
      } else {
        this.possibleMoves = [];
      }
    });
  }

  private isFigureSelectable(figure: ChessFigure) {
    return figure.figureColor === this.currentTurn;
  }

  private isFigureAlreadySelected(figure: ChessFigure) {
    return this.selectedFigure?.position === figure.position;
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
