import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChessFigure } from '../../../../../core/models/chess/logic/chessfigure';
import { ChessboardFieldComponent } from '../chessboard-field/chessboard-field.component';

@Component({
  selector: 'app-chessboard',
  templateUrl: './chessboard.component.html',
  styleUrls: ['./chessboard.component.css'],
  standalone: true,
  imports: [
    CommonModule, 
    ChessboardFieldComponent
  ]
})

export class ChessboardComponent {
  @Input() board: (ChessFigure | null)[][] = [];
  @Input() currentTurn: string | undefined;
  @Input() selectedFigure: ChessFigure | null = null;
  @Input() possibleMoves: { col: string, row: number }[] = [];

  @Output() onFieldClicked = new EventEmitter<{col: number, row: number}>();

  fieldClicked(row: number, column: number) {
    this.onFieldClicked.emit({col: column, row: row});
  }
}
