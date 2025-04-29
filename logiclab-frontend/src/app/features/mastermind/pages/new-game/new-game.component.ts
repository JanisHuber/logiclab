import { Component, OnInit } from '@angular/core';
import { GameService } from '../../../../core/services/mastermind/game.service';
import { RouterModule, Router } from '@angular/router';


@Component({
  selector: 'app-new-game',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './new-game.component.html',
  styleUrl: './new-game.component.css'
})
export class NewGameComponent {

  constructor(
    private gameService: GameService,
    private router: Router
  ) {}

  startNewGame() {
    this.gameService.startNewGame().subscribe(
      (game) => {
        this.router.navigate(['/mastermind/' + game.gameId]);
      }
    );
  }
}
