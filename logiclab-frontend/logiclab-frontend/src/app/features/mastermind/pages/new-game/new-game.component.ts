import { Component, OnInit } from '@angular/core';
import { GameService } from '../../../../core/services/mastermind/game.service';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router';
import { GameDetailsComponent } from "../game-details/game-details.component";


@Component({
  selector: 'app-new-game',
  standalone: true,
  imports: [RouterModule, HttpClientModule, GameDetailsComponent],
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
