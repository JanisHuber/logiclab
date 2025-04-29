import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChessFigure } from '../../../../../core/models/chess/logic/chessfigure';
import { ChessfigureComponent } from '../chessfigure/chessfigure.component';
import { boardToApiPosition } from '../../../../../shared/shared/chess.helpers';
import { Field } from '../../../../../core/models/chess/dto/field';

@Component({
  selector: 'app-chessboard',
  templateUrl: './chessboard.component.html',
  styleUrls: ['./chessboard.component.css'],
  standalone: true,
  imports: [
    CommonModule, 
    ChessfigureComponent
  ]
})

export class ChessboardComponent {
  @Input() board: (ChessFigure | null)[][] = [];
  @Input() currentTurn: string | undefined;
  @Input() selectedFigure: ChessFigure | null = null;
  @Input() possibleMoves: { row: string, column: number }[] = [];

  @Output() figureClicked = new EventEmitter<ChessFigure>();
  @Output() moveMade = new EventEmitter<{row: number, column: number}>();
  @Output() startedNewGame = new EventEmitter<void>();

  onFigureClicked(figure: ChessFigure) {
    this.figureClicked.emit(figure);
  }

  makeMove(row: number, column: number) {
    this.moveMade.emit({row: row, column: column});
  }

  isValidMove(row: number, column: number): boolean {
    const apiPosition = boardToApiPosition(row, column);
    return this.possibleMoves.some(move => 
      move.row === apiPosition.convertedRow && move.column === apiPosition.convertedColumn
    );
  }

  startNewGame() {
    //todo resign
    this.startedNewGame.emit();
  }
}
