import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {ContactService} from '../conatct.service';
import {CategorieService} from '../categorie.service';
import {Observable} from 'rxjs';
import {Contact} from './Contact';
import {FormsModule} from '@angular/forms';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  user: { nom: string, prenom: string } | null = null;
  contacts: Contact[] = [];
  status: string = '';
  message: string = '';

  constructor(
    private contactService: ContactService,
    private authService: AuthService,
    private router: Router,
    private http: HttpClient,
    private categorieService: CategorieService
  ) {}

  ngOnInit() {

    this.user = this.authService.getUserDetails();

    this.contactService.getContacts().subscribe({
      next: (contacts: Contact[]) => {
        this.contacts = contacts;
      },
      error: (error: any) => {
        console.log(error);
      }
    })

  }

  logout() {
    this.authService.logout().subscribe();
  }
  addcontact(){
    this.router.navigate(['/contact/add-contact'])
  }


  updatecontact(contact: Contact){
    sessionStorage.setItem("contact", JSON.stringify(contact));
    this.router.navigate(['/contact/updatecontact'])
  }
  deletecontact(contact: Contact){
    const token = localStorage.getItem('token');


    if (!token) {
      this.status = 'error';
      this.message = 'No authentication token found. Please log in again.';
      return;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    this.http.post<any>('http://localhost:8080/contact/delete',contact, { headers }).subscribe({
      next: (response) => {
        this.status = response.status;
        this.message = response.message;

        if(response.status === 'success'){
          setTimeout(() => {
            window.location.reload();
          },500);
        }

      },
      error: (error: any) => {
        this.status = 'error';
        this.message = error.message;
      }
    })
  }


  showCategoryForm = false;
  newCategory: any = {
    nom: '',
    description: ''
  };

  toggleCategoryForm() {
    this.showCategoryForm = !this.showCategoryForm;
    if (!this.showCategoryForm) {
      this.resetCategoryForm();
    }
  }

  resetCategoryForm() {
    this.newCategory = {
      nom: '',
      description: ''
    };
  }

  createCategory() {
    if (!this.newCategory.nom.trim()) return;

    this.categorieService.createCategory(this.newCategory).subscribe({
      next: (createdCategory) => {
        // Handle success (update your categories list if needed)
        this.message = `Category "${createdCategory.nom}" created successfully!`;
        this.resetCategoryForm();
        this.showCategoryForm = false;

        // If you maintain a categories list in component:
        // this.categories.push(createdCategory);
      },
      error: (err) => {
        this.message = 'Error creating category: ' + (err.error?.message || '');
        console.error('Category creation failed', err);
      }
    });
  }


}
