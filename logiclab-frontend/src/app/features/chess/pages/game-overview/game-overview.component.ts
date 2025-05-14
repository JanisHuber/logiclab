import { Component, Input, OnInit } from '@angular/core';
import { GameService } from '../../../../core/services/chess/game.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-game-overview',
  imports: [],
  templateUrl: './game-overview.component.html',
  styleUrl: './game-overview.component.css'
})
export class GameOverviewComponent {
  @Input() winner: string | undefined;
  @Input() gameId: number | undefined;

  constructor(private gameService: GameService, private router: Router) { }

  startNewGame() {
    this.router.navigate(['/chess/new']);
  }
}
