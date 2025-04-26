import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import * as XLSX from 'xlsx';
import {Contact} from '../../app/home/Contact';

@Component({
  selector: 'app-update-contact',
  imports: [CommonModule,FormsModule],
  templateUrl: './update-contact.component.html',
  standalone: true,
  styleUrl: './update-contact.component.css'
})
export class UpdateContactComponent {
  contact_1 = {
    email: '',
    firstName: '',
    lastName: '',
  }

  message = '';
  status = '';

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.loadContact();
  }

  loadContact() {
    const stored = sessionStorage.getItem("contact");
    if (stored) {
      const contact: Contact = JSON.parse(stored);
      this.contact_1.email = contact.email;
      this.contact_1.firstName = contact.firstName;
      this.contact_1.lastName = contact.lastName;
    }
  }





  updateContact() {
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

    this.http.post<any>('http://localhost:8080/contact/update',this.contact_1,{ headers })
      .subscribe({
        next: (response) => {
          this.status = response.status;
          this.message = response.message;
        },
        error: (err) =>{
          this.status = 'error';
          this.message = err.message;
        }
      })
  }

}
