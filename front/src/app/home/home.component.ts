import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {ContactService} from '../conatct.service';
import {Observable} from 'rxjs';
import {Contact} from './Contact';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
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
    private http: HttpClient
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
          },1500);
        }

      },
      error: (error: any) => {
        this.status = 'error';
        this.message = error.message;
      }
    })
  }

}
