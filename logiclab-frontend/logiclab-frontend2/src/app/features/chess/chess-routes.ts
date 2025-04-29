import { Routes } from '@angular/router';
import { NewGameComponent } from './pages/new-game/new-game.component';


export const CHESS_ROUTES: Routes = [
  {
    path: 'new',
    component: NewGameComponent,
  }
];


export default CHESS_ROUTES;