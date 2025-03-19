import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiurl = "http://localhost:8080/auth";
  constructor(private http: HttpClient,private router: Router) { }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiurl}/register`, user);
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.apiurl}/login`, user);
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiurl}/logout`, {}).pipe(
      tap(() => {
        localStorage.removeItem('session');
        localStorage.removeItem('nom');
        localStorage.removeItem('prenom');
        this.router.navigate(['/login']);
      })
    );
  }

  checkSession(): Observable<any> {
    return this.http.get(`${this.apiurl}/check-session`);
  }
}
