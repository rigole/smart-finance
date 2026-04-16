import { authGuard } from "../shared/guards/auth.guard";
import { LoginComponent } from "./login/login.component";
import { RegisterComponent } from "./register/register.component";


export const authRoutes = [

    {
        path: 'login',
        loadComponent: () => import('./login/login.component').
            then(m => m.LoginComponent)
    },
    {
        path: 'register',
        loadComponent: () => import('./register/register.component').
            then(m => m.RegisterComponent)
    },
    {
        path: 'profile/:id',
        loadComponent: () => import('./profile/profile.component').
            then(m => m.ProfileComponent),
        children: [
            {
                path: '',
                loadComponent: () => import('./overview/overview.component').
                    then(m => m.OverviewComponent)
            },
            {
                path: 'transactions',
                loadComponent: () => import('../transactions/transaction-list/transaction-list.component').
                    then(m => m.TransactionListComponent)
            },
            {
                path: 'budgets',
                loadComponent: () => import('../budgets/budget-list/budget-list.component').
                    then(m => m.BudgetListComponent)
            },
            {
                path: 'insights',
                loadComponent: () => import('../insights/insights-list/insights-list.component').
                    then(m => m.InsightsListComponent)
            }
        ],
        canActivate: [authGuard]
    }

];