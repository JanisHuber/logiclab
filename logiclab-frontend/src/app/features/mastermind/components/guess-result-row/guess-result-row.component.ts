import { Component, EventEmitter, Output } from '@angular/core';
import { Guess } from '../../../../core/models/mastermind/guess';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { guessToColors } from '../../../../shared/shared/mastermind.helpers';

@Component({
  selector: 'app-guess-result-row',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './guess-result-row.component.html',
  styleUrl: './guess-result-row.component.css'
})
export class GuessResultRowComponent {
  @Input() guessList: Guess[] = [];

  @Output() guessClicked: EventEmitter<Guess> = new EventEmitter<Guess>();

  onGuessClick(guess: Guess) {
    this.guessClicked.emit(guess);
  }

  guessToColors(guess: Guess): string[] {
    return guessToColors(guess.guess);
  }
}
