import {inject, Injectable} from '@angular/core';
import {
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  CanActivateFn,
  CanActivate
} from '@angular/router';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    console.log('AuthGuard checking route:', state.url);
    const token = this.authService.getToken();

    if (token) {
      return true;
    }

    // Redirect to login page
    this.router.navigate(['/login']);
    return false;
  }
}


export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authGuardService = inject(AuthGuardService);
  return authGuardService.canActivate(route, state);
};
