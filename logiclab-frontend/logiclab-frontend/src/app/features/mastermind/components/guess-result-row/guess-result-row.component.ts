import { Component, EventEmitter, Output } from '@angular/core';
import { Guess } from '../../../../core/models/mastermind/guess';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserGuess } from '../../../../core/models/mastermind/user-guess';


@Component({
  selector: 'app-guess-result-row',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './guess-result-row.component.html',
  styleUrl: './guess-result-row.component.css'
})
export class GuessResultRowComponent {
  @Input() guessList: UserGuess[] = [];

  @Output() guessClicked: EventEmitter<UserGuess> = new EventEmitter<UserGuess>();

  onGuessClick(UserGuess: UserGuess) {
    this.guessClicked.emit(UserGuess);
  }


}
