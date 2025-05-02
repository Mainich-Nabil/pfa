import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Categorie, Contact} from './home/Contact';
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



  header(){
    const token = this.authService.getToken();


    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return {headers};
  }


  createCategory(categoryData: { nom: string, description: string }): Observable<any> {

    return this.http.post(`${this.url}/add-categorie`, categoryData,this.header());
  }

  addcontacttocat(clients: Set<Contact>,nomCat: String): Observable<any> {
    return this.http.post(`${this.url}/add-contacts/${nomCat}`, clients, this.header());
  }
  getCategories(){
    return this.http.get<Categorie[]>(`${this.url}/get-categories`,this.header());
  }

  deleteCategorie(nomCat:String): Observable<any>{
    return this.http.get(`${this.url}/delete/${nomCat}`,this.header());
  }

  deleteContactFromCat(nomCat:String,conEmail:String): Observable<any>{
    return this.http.post(`${this.url}/delete-cat/${nomCat}`,conEmail,this.header());
  }

  editCategorie(updated: {nom: string, description: string}, nomCat:String): Observable<any> {
    return this.http.post(`${this.url}/update/${nomCat}`,updated,this.header());
  }


}
