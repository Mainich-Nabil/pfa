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

  headers(){
    const token = this.authService.getToken();


    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return {headers};
  }

  getContacts(): Observable<Contact[]> {
    return this.http.get<Contact[]>(`${this.url}/get-contacts`, this.headers());
  }

  getContactsBycat(catName: string){
    return this.http.get<Contact[]>(`${this.url}/get-contacts/${catName}`,this.headers());
  }




}
