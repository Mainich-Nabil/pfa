import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';

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

  logout(): void {
    this.http.delete(`${this.apiurl}/logout`);
    localStorage.removeItem('session');
    this.router.navigate(['/login'])
  }

  checkSession(): Observable<any> {
    return this.http.get(`${this.apiurl}/check-session`);
  }



}
