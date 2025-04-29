import { Component, OnInit } from '@angular/core';
import { GameService } from '../../../../core/services/mastermind/game.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Game } from '../../../../core/models/mastermind/game';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Guess } from '../../../../core/models/mastermind/guess';
import { prettyPrintError } from '../../../../shared/shared/pretty-print-errors.helpers';
import { GuessResultRowComponent } from "../../components/guess-result-row/guess-result-row.component";
import { UserInput } from '../../../../core/models/mastermind/user-input';
import { UserGuess } from '../../../../core/models/mastermind/user-guess';


@Component({
  selector: 'app-game-details',
  standalone: true,
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    GuessResultRowComponent
],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent implements OnInit{

  game: Game | undefined;
  gameId: number | undefined;
  guessesAttemptsLeft: number | undefined;

  userGuessResults: UserGuess[] = [];
  userGuessString: string = '';
  
  RequestError: string = '';

  //Inputs
  colorOptions: string[] = (['bg-red-500', 'bg-blue-500', 'bg-green-500', 'bg-yellow-500', 'bg-purple-500', 'bg-orange-500', 'bg-pink-500', 'bg-teal-500', 'bg-gray-500']);
  showButton: boolean = false;
  userInputs: UserInput[] = [];
  userGuessColor: string = '';

  constructor(
    private gameService: GameService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.updateGameDetails();
  }

  submitGuess() {
    if (!this.isGuessValid(this.userGuessString)) {
      this.RequestError = 'Invalid guess format. Please enter a 4-digit number.';
      return;
    }
    if (this.userGuessResults.length >= 8) {
      this.RequestError = 'Maximum number of guesses reached.';
      return;
    }
    if (this.gameId !== undefined) {
      this.gameService.submitGuess(this.gameId, this.userGuessString).subscribe(guess => {
        this.userGuessResults.push({guess: guess.guess, correctPositions: guess.correctPositions, correctNumbers: guess.correctNumbers, colors: this.userGuessColor.split(', ')});
        this.RequestError = ''; 
        this.updateGameDetails();
      }, error => {
        this.RequestError = prettyPrintError(error.status);
      });
    } else {
      console.error('Game ID is undefined. Cannot submit guess.');
    }
  }

  updateGameDetails() {
    this.gameId = Number(this.route.snapshot.paramMap.get('id'));
    this.gameService.getGameDetails(this.gameId).subscribe(game => {
      this.game = game;
      this.guessesAttemptsLeft = 8 - game.guesses.length;
      this.userGuessResults = game.guesses.map(guess => {
        return {guess: guess.guess, correctPositions: guess.correctPositions, correctNumbers: guess.correctNumbers, colors: this.splitGuessToColors(guess.guess)};
      });
    });
  }

  removeResults(clickedGuess: UserGuess) {
    this.userGuessResults = this.userGuessResults.filter(userGuess => userGuess !== clickedGuess);
  }

  isGuessValid(guess: string): boolean {
    const regex = /^[0-9]{4}$/; 
    return regex.test(guess);
  }

  selectItem(color: string, row: number) {
    this.userInputs[row] = { guessNumber: this.colorOptions.findIndex(option => option === color), color: color };
    this.showButton = true;

    this.userGuessColor = this.userInputs.map(input => input.color).join(', ');
    this.userGuessString = this.userInputs.map(input => input.guessNumber).join('');
  }

  splitGuessToColors(guess: string): string[] {
    let colors: string[] = [];
    for (const c of guess) {
      const index = parseInt(c, 10);
      colors.push(this.colorOptions[index]);
    }
    return colors;
  }
}