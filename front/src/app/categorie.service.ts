import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Contact} from './home/Contact';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CategorieService {
  private url = 'http://localhost:8080/Categorie';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  createCategory(categoryData: { nom: string, description: string }): Observable<any> {
    return this.http.post(`${this.url}/categories`, categoryData);
  }
}
