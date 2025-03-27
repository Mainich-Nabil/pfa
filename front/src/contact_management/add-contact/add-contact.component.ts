import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'; // Import HttpHeaders
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-contact',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-contact.component.html',
  standalone: true,
  styleUrl: './add-contact.component.css'
})
export class AddContactComponent {
  contact = {
    firstName: '',
    lastName: '',
    email: ''
  };
  message = '';
  status = '';

  constructor(private http: HttpClient) {}

  SubmitContact() {
    // Create a Set containing the contact data
    const contactSet = new Set([this.contact]);
    console.log('Contact data prepared for submission:', Array.from(contactSet)); // Debugging log

    // Retrieve the token from localStorage
    const token = localStorage.getItem('token');
    console.log('Token retrieved from localStorage:', token); // Debugging log

    if (!token) {
      console.error('No token found in localStorage. Please log in again.'); // Debugging log
      this.status = 'error';
      this.message = 'No authentication token found. Please log in again.';
      return;
    }

    // Create headers with the Authorization token
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    console.log('Headers created for HTTP request:', headers); // Debugging log

    // Make the HTTP POST request with the headers
    this.http.post<any>('http://localhost:8080/contact/add', Array.from(contactSet), { headers })
      .subscribe({
        next: (response) => {
          console.log('HTTP request successful. Response:', response); // Debugging log
          this.status = response.status || 'success';
          this.message = response.message || 'Contact added successfully.';

          // Reset the form
          this.contact = {
            firstName: '',
            lastName: '',
            email: ''
          };
        },
        error: (err) => {
          console.error('HTTP request failed. Error response:', err); // Debugging log
          this.status = 'error';
          this.message = err.message || 'An error occurred while submitting the contact.';
        }
      });
  }
}
