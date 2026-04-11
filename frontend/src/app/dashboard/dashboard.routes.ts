
export const dashboardRoutes = [
    {
        path: '',
        loadComponent: () => import('./home/home.component').
        then(m => m.HomeComponent)
    }
    
];