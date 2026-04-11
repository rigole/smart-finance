import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadChildren: () => import('./dashboard/dashboard.routes').
        then(m => m.dashboardRoutes)
    },
    {
        path: 'auth',
        loadChildren: () => import('./auth/auth.routes').
        then(m => m.authRoutes)
    }
];
