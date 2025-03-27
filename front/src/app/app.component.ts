import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';

import {AuthService} from './auth.service';
import { firstValueFrom, interval, of, Subject, switchMap, takeUntil} from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  private stopPolling = new Subject<void>();

  constructor(private authService: AuthService, private router: Router) {}

  async ngOnInit() {
    try {
      const isAuthenticated = await firstValueFrom(this.authService.checkSession());
      if (!isAuthenticated) {
        this.router.navigate(['/login']);
      }
    } catch (error) {
      this.router.navigate(['/login']);
    }

    interval(300000)
      .pipe(
        switchMap(() => this.authService.checkSession()),
        takeUntil(this.stopPolling)
      )
      .subscribe(isAuthenticated => {
        if (!isAuthenticated) {
          this.router.navigate(['/login']);
        }
      });
  }

  ngOnDestroy() {
    this.stopPolling.next();
    this.stopPolling.complete();
  }
}
