import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GameService } from '../../../../core/services/chess/game.service';

@Component({
  selector: 'app-new-game',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css']
})
export class NewGameComponent {
  gameMode: 'player' | 'bot' = 'player';
  botDifficulty: 'easy' | 'medium' | 'hard' = 'medium';
  playerColor: 'white' | 'black' | 'random' = 'white';

  constructor(private gameService: GameService, private router: Router) {}

  startNewGame() {
    let botDifficultyNumber = 0;
    if (this.botDifficulty === 'easy') {
      botDifficultyNumber = 5;
    } else if (this.botDifficulty === 'medium') {
      botDifficultyNumber = 6;
    } else if (this.botDifficulty === 'hard') {
      botDifficultyNumber = 7;
    }

    const params = {
      againstAI: this.gameMode === 'bot',
      botDifficulty: botDifficultyNumber,
      botColor: this.playerColor === 'white' ? 'BLACK' : 'WHITE'
    };

    this.gameService.startNewGame(params.againstAI, params.botColor, params.botDifficulty).subscribe((response: any) => {
      this.router.navigate(['/chess/', response.gameId], { replaceUrl: true });
    });
  }
}