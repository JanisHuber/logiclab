import { Routes } from '@angular/router';
import { NewGameComponent } from './pages/new-game/new-game.component';
import { GameDetailsComponent } from './pages/game-details/game-details.component';


export const CHESS_ROUTES: Routes = [
  {
    path: 'new',
    component: NewGameComponent,
  },
  {
    path: ':gameId',
    component: GameDetailsComponent
  }
];

export default CHESS_ROUTES;