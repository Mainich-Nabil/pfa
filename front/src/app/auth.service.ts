import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = "http://localhost:8080/auth";

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}


  private setToken(token: string): void {
    localStorage.setItem('token', token);
  }


  getToken(): string | null {
    return localStorage.getItem('token');
  }


  private clearAuthData(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('nom');
    localStorage.removeItem('prenom');
  }


  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }


  login(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user).pipe(
      map((response: any) => {
        if (response.status === 'success' && response.token) {

          this.setToken(response.token);
          localStorage.setItem('nom', response.nom);
          localStorage.setItem('prenom', response.prenom);
        }
        return response;
      }),
      catchError(error => {
        console.error('Login error', error);
        return throwError(() => error);
      })
    );
  }

  // Logout method
  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, {}).pipe(
      tap(() => {

        this.clearAuthData();

        this.router.navigate(['/login']);
      }),
      catchError(error => {

        this.clearAuthData();
        this.router.navigate(['/login']);
        return throwError(() => error);
      })
    );
  }

  checkSession(): Observable<boolean> {
    const token = this.getToken();

    if (!token) {
      return of(false);
    }

    // Add Authorization header with Bearer token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<any>(`${this.apiUrl}/check-session`, { headers }).pipe(
      map(response => response.status === 'success'),
      catchError(() => of(false))
    );
  }

  // Get user details
  getUserDetails(): { nom: string, prenom: string } | null {
    const nom = localStorage.getItem('nom');
    const prenom = localStorage.getItem('prenom');

    return nom && prenom ? { nom, prenom } : null;
  }
}
