import { Routes } from '@angular/router';
import { NewGameComponent } from './pages/new-game/new-game.component';
import { GameDetailsComponent } from './pages/game-details/game-details.component';

export const MASTERMIND_ROUTES: Routes = [
  {
    path: 'new',
    component: NewGameComponent,
  },
  {
    path: ':id',
    component: GameDetailsComponent
  }
];


export default MASTERMIND_ROUTES;