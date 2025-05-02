import {Component, HostListener, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {ContactService} from '../conatct.service';
import {CategorieService} from '../categorie.service';
import {Observable} from 'rxjs';
import {Categorie, Contact} from './Contact';
import {FormsModule} from '@angular/forms';
import {fadeAnimation} from '../animations';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  animations: [fadeAnimation]
})
export class HomeComponent implements OnInit {
  user: { nom: string, prenom: string } | null = null;
  contacts: Contact[] = [];
  selectedCategory: string = '';
  categories: Categorie[] = [];
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


    this.getContacts();

    this.categorieService.getCategories().subscribe({
      next: (categories: Categorie[]) => {
        this.categories = categories;
        console.log(this.categories);
      },
      error: (error: any) => {
        console.log(error);
      }
    })

  }

  getContacts(){
    this.contactService.getContacts().subscribe({
      next: (contacts: Contact[]) => {
        this.contacts = contacts;
      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }

  getContactsByCategory(category: string){
    this.contactService.getContactsBycat(category).subscribe({
      next: (contacts: Contact[]) => {
        this.contacts = contacts;
      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }

  applyCategoryFilter(){
    if(this.selectedCategory !== ''){
      this.getContactsByCategory(this.selectedCategory);
    }else {
      this.getContacts();
    }
  }

  logout() {
    this.authService.logout().subscribe();
  }
  addcontact(){
    this.router.navigate(['/contact/add-contact'])
  }
  ManageCats(){
    this.router.navigate(['category-Management'])
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
      next: (responce) => {

        this.status = responce.status;
        this.message = responce.message;
        this.resetCategoryForm();
        this.showCategoryForm = false;
        if(responce.status === 'success'){
          setTimeout(() => {
            window.location.reload();
          },500);
        }



      },
      error: (err) => {
        this.message = 'Error creating category: ' + (err.error?.message || '');
        console.error('Category creation failed', err);
      }
    });
  }

  // Stocke l'email du client dont le menu est ouvert
  selectedDropdownClientEmail: string | null = null;

// Gestion du clic sur le bouton "Category"
  toggleDropdown(client: any): void {
    this.selectedDropdownClientEmail =
      this.selectedDropdownClientEmail === client.email ? null : client.email;
  }

// Ferme le menu si on clique en dehors
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.dropdown-toggle')) {
      this.selectedDropdownClientEmail = null;
    }
  }

  selectCategory(client: any, nomCat: string) {

    this.categorieService.addcontacttocat(client, nomCat).subscribe({
      next: (responce) => {
        this.status = responce.status;
        this.message = responce.message;

        if(responce.status === 'success'){
          setTimeout(() => {
            window.location.reload();
          },500)
        }

      }
    })
  }




}
