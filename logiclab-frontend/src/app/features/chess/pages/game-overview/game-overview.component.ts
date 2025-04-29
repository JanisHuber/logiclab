import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-game-overview',
  imports: [],
  templateUrl: './game-overview.component.html',
  styleUrl: './game-overview.component.css'
})
export class GameOverviewComponent {
  @Input() winner: string | undefined;
}
