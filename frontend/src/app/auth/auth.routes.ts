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
        canActivate: [authGuard]
    }

];