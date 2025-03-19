import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {AuthGuard} from './auth/auth.guard';
import {AuthService} from './auth.service';
import {catchError, firstValueFrom, interval, of, Subject, switchMap, takeUntil} from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit,OnDestroy {

  private stopPolling = new Subject<void>();

  constructor(private authservice: AuthService, private router: Router) {
  }
  async ngOnInit() {
    const responce = await firstValueFrom(this.authservice.checkSession());

      try{
        const response = await firstValueFrom(this.authservice.checkSession())
        if(response.status !== 'success') {
          this.router.navigate(['/login']);
        }
      }catch (error: any) {
        this.router.navigate(['/login']);
      }
      interval(300000)
        .pipe(
          switchMap(() => this.authservice.checkSession()),
          catchError(() => of({status: 'error'})),
          takeUntil(this.stopPolling)
        )
        .subscribe(async response => {
          if(response.status !== 'success') {
            this.router.navigate(['/login']);
          }
        });
  }
  ngOnDestroy() {
    this.stopPolling.next();
    this.stopPolling.complete();
  }
}
