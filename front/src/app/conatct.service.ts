import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Contact} from './home/Contact';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private url = 'http://localhost:8080/contact';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getContacts(): Observable<Contact[]> {
    const token = this.authService.getToken();


    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });


    return this.http.get<Contact[]>(`${this.url}/get-contacts`, { headers });
  }
}
