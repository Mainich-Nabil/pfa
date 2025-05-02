import { Component, OnInit } from '@angular/core';
import { Categorie, Contact } from '../home/Contact';
import { ContactService } from '../conatct.service';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CategorieService } from '../categorie.service';
import { CommonModule } from '@angular/common';
import { animate, style, transition, trigger } from '@angular/animations';
import { forkJoin } from 'rxjs';
import {FormsModule} from '@angular/forms';
import {fadeAnimation} from '../animations';

@Component({
  selector: 'app-categorie-management',
  imports: [CommonModule, FormsModule],
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({ height: '0', opacity: 0 }),
        animate('200ms ease-in', style({ height: '*', opacity: 1 }))
      ]),
      transition(':leave', [
        animate('200ms ease-out', style({ height: '0', opacity: 0 }))
      ])
    ]),
    fadeAnimation
  ],
  templateUrl: './categorie-management.component.html',
  standalone: true,
  styleUrl: './categorie-management.component.css'
})
export class CategorieManagementComponent implements OnInit {
  user: { nom: string, prenom: string } | null = null;
  categories: (Categorie & { isOpen?: boolean })[] = [];
  contacts: Contact[] = [];
  contactCounts: { [key: string]: number } = {};
  loading = true;
  status: string = '';
  message: string = '';

  editingCategory: boolean = false;
  currentCategory: string = '';
  updatedCategory: { nom: string; description: string } = {
    nom: '',
    description: ''
  };
  originalCategory: { nom: string; description: string } = {
    nom: '',
    description: ''
  };

  constructor(
    private contactService: ContactService,
    private authService: AuthService,
    private router: Router,
    private http: HttpClient,
    private categorieService: CategorieService
  ) {}

  ngOnInit() {
    this.user = this.authService.getUserDetails();
    this.loadData();
  }

  loadData() {
    this.loading = true;
    forkJoin([
      this.categorieService.getCategories(),
      this.contactService.getContacts()
    ]).subscribe({
      next: ([categories, contacts]) => {
        this.categories = categories.map(cat => ({ ...cat, isOpen: false }));
        this.contacts = contacts;
        this.calculateContactCounts();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading data:', error);
        this.loading = false;
      }
    });
  }

  calculateContactCounts() {
    this.contactCounts = {};
    this.categories.forEach(category => {
      this.contactCounts[category.nom] = this.contacts.filter(contact =>
        Array.from(contact.categories || []).some(cat => cat.nom === category.nom)
      ).length;
    });
  }

  toggleCategory(categoryName: string) {
    const category = this.categories.find(cat => cat.nom === categoryName);
    if (category) {
      category.isOpen = !category.isOpen;
    }
  }

  getContactsForCategory(categoryName: string): Contact[] {
    return this.contacts.filter(contact =>
      Array.from(contact.categories || []).some(cat => cat.nom === categoryName)
    );
  }

  deleteCategory(categoryName: string) {
     this.categorieService.deleteCategorie(categoryName).subscribe({
       next: (response) => {
         this.status = response.status;
         this.message = response.message;
         if(response.status === 'success'){
           setTimeout(() => {
             window.location.reload();
           },500);
         }
       },
       error:err => {
         this.status = err.status;
         this.message = err.message;
       }
     })
  }

  deleteContact(categoryName: string,contactemail: string) {
    this.categorieService.deleteContactFromCat(categoryName, contactemail).subscribe({
      next: (response) => {
        this.message = response.message;
        this.status = response.status;
        if(response.status === 'success'){
          setTimeout(() => {
            window.location.reload();
          },500);
        }
      },
      error: err => {
        this.status = err.status;
        this.message = err.message;
      }
    })
  }

  addCategory() {
    // pass
  }

  logout() {
    this.authService.logout().subscribe();
  }

  Home() {
    this.router.navigate(['/home']);
  }

  editCategory(category: any) {
    this.editingCategory = true;
    this.currentCategory = category.nom;

    // Initialize the form with current category values
    this.updatedCategory = {
      nom: category.nom,
      description: category.description
    };

    // Save original values for comparison
    this.originalCategory = {
      nom: category.nom,
      description: category.description
    };
  }

// Function to get category description by name
  getCategoryDescription(categoryName: string): string {
    const category = this.categories.find(c => c.nom === categoryName);
    return category ? category.description : '';
  }

// Function to cancel editing
  cancelEdit() {
    this.editingCategory = false;
    this.currentCategory = '';
    this.updatedCategory = {
      nom: '',
      description: ''
    };
  }

// Function to update the category
  updateCategory() {
    // Validate form
    if (!this.updatedCategory.nom.trim()) {
      this.message = 'Category name cannot be empty';
      this.status = 'error';
      return;
    }

    // Check if anything changed
    if (this.updatedCategory.nom === this.originalCategory.nom &&
      this.updatedCategory.description === this.originalCategory.description) {
      this.cancelEdit();
      return;
    }

    this.categorieService.editCategorie(this.updatedCategory,this.originalCategory.nom).subscribe({
      next: (response) => {
        this.status = response.status;
        this.message = response.message;

        if(response.status === 'success'){
          setTimeout(() => {
            window.location.reload();
          },500)
        }
      },
      error: (err) => {
        this.status = err.status;
        this.message = err.message;
      }
    })




    // Close the edit form after updating
    this.cancelEdit();
  }
}
