import { Component, OnInit } from '@angular/core';
import { GameService } from '../../../../core/services/mastermind/game.service';
import { Game } from '../../../../core/models/mastermind/game';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { prettyPrintError } from '../../../../shared/shared/pretty-print-errors.helpers';
import { GuessResultRowComponent } from "../../components/guess-result-row/guess-result-row.component";
import { CodePickerComponent } from '../../components/code-picker/code-picker.component';


@Component({
  selector: 'app-game-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    GuessResultRowComponent,
    CodePickerComponent
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})

export class GameDetailsComponent implements OnInit{

  game: Game = {
    gameId: 0,
    gameState: 'notThere',
    guesses: []
  };
  gameId: number | undefined;
  userGuessString: string = '';
  RequestError: string = '';
  
  constructor(
    private gameService: GameService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.updateGameDetails();
  }

  colorsSelected(guess: string) {
    console.log('Selected colors:', guess);
    this.userGuessString = guess;
  }

  submitGuess() {
    if (this.game.gameState !== 'started') {
      this.RequestError = 'Maximum number of guesses reached.';
      return;
    }
    if (this.gameId !== undefined) {
      this.gameService.submitGuess(this.gameId, this.userGuessString).subscribe(guess => {
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
    });
  }

  guessesLeft(): number {
    return 8 - this.game.guesses.length;
  }
}