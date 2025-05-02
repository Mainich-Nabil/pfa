import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './auth/auth.guard';
import { AddContactComponent } from '../contact_management/add-contact/add-contact.component';
import {UpdateContactComponent} from '../contact_management/update-contact/update-contact.component';
import {CategorieManagementComponent} from './categorie-management/categorie-management.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  { path: 'contact/add-contact', component: AddContactComponent, canActivate: [authGuard] },
  { path: 'contact/updatecontact', component: UpdateContactComponent, canActivate: [authGuard] },
  { path: 'category-Management', component: CategorieManagementComponent, canActivate: [authGuard] }
];
