import { Component, Input } from '@angular/core';
import { GameService } from '../../../../core/services/chess/game.service';

@Component({
  selector: 'app-game-overview',
  imports: [],
  templateUrl: './game-overview.component.html',
  styleUrl: './game-overview.component.css'
})
export class GameOverviewComponent {
  @Input() winner: string | undefined;
  @Input() gameId: number | undefined;

  moveHistory: string = '';

  constructor(private gameService: GameService) { }
  
  ngOnInit() {
    if (!this.gameId) return;
    this.gameService.getMoveHistory(this.gameId).subscribe((history) => {
      this.moveHistory = history;
    });  
  }
}
