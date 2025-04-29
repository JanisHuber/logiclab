import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChessFigure } from '../../../../../core/models/chess/logic/chessfigure';

@Component({
  selector: 'app-chessfigure',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chessfigure.component.html',
  styleUrls: ['./chessfigure.component.css']
})
export class ChessfigureComponent {
  @Input() figure: ChessFigure | null = null;

  @Output() figureClicked = new EventEmitter<ChessFigure>();

  getFigureImage(): string {
    if (!this.figure) return '';
    const color = this.figure.figureColor.toLowerCase();
    const type = this.figure.type.toLowerCase();
    return `assets/chess/figure_${color}_${type}.png`;
  }

  onClick() {
    if (this.figure) {
      this.figureClicked.emit(this.figure);
    }
  }
}
