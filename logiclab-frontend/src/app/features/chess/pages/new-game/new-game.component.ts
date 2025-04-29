import { Component } from '@angular/core';
import { GameService } from '../../../../core/services/chess/game.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-game',
  imports: [],
  templateUrl: './new-game.component.html',
  styleUrl: './new-game.component.css'
})
export class NewGameComponent {
  constructor(private gameService: GameService, private router: Router) {}

  startNewGame() {
    this.gameService.startNewGame().subscribe((game) => {
      this.router.navigate(['/chess/', game.gameId]);
    });
  }
}