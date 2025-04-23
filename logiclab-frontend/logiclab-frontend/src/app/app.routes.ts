import { Routes } from '@angular/router';


export const routes: Routes = [
    {
        path: 'mastermind',
        loadChildren: () =>
          import('./features/mastermind/mastermind-routes').then((m) => m.default)
    },
    {
        path: 'chess',
        loadChildren: () =>
          import('./features/chess/chess-routes').then((m) => m.default)
    }
];
