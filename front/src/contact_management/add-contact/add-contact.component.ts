import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import * as XLSX from 'xlsx';

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
  contactSet = new Set<any>();
  fileName = '';

  constructor(private http: HttpClient) {}

  onFileChange(event: any) {
    const target: DataTransfer = <DataTransfer>(event.target);

    if (target.files.length !== 1) {
      this.message = 'You can only upload one file at a time';
      this.status = 'error';
      return;
    }

    const file = target.files[0];
    this.fileName = file.name;

    const reader: FileReader = new FileReader();
    reader.onload = (e: any) => {

      const arrayBuffer = e.target.result;
      const workbook = XLSX.read(arrayBuffer, { type: 'array' });

      const firstSheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[firstSheetName];


      const excelData = XLSX.utils.sheet_to_json(worksheet);


      const mappedData = excelData.map((row: any) => {
        return {
          firstName: row['nom'] || row['firstName'] || row['Nom'] || row['First Name'] || '',
          lastName: row['penom'] || row['lastName'] || row['Prenom'] || row['Last Name'] || '',
          email: row['email'] || row['Email'] || row['E-mail'] || ''
        };
      });


      this.contactSet = new Set([...Array.from(this.contactSet), ...mappedData]);
      this.message = `Successfully imported ${mappedData.length} contacts from Excel`;
      this.status = 'success';
    };


    reader.readAsArrayBuffer(file);
  }

  addCurrentContact() {
    if (!this.contact.firstName || !this.contact.email) {
      this.message = 'First name and email are required';
      this.status = 'error';
      return;
    }

    this.contactSet.add({...this.contact});
    this.message = 'Contact added to the list';
    this.status = 'success';

    // Clear the form
    this.contact = {
      firstName: '',
      lastName: '',
      email: ''
    };
  }

  SubmitContact() {

    if (this.contact.firstName || this.contact.lastName || this.contact.email) {
      this.addCurrentContact();
    }

    if (this.contactSet.size === 0) {
      this.message = 'No contacts to submit';
      this.status = 'error';
      return;
    }

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

    this.http.post<any>('http://localhost:8080/contact/add', Array.from(this.contactSet), { headers })
      .subscribe({
        next: (response) => {
          this.status = response.status || 'success';
          this.message = response.message || `${this.contactSet.size} contacts added successfully.`;


          this.contact = {
            firstName: '',
            lastName: '',
            email: ''
          };
          this.contactSet.clear();
          this.fileName = '';
        },
        error: (err) => {
          this.status = 'error';
          this.message = err.message || 'An error occurred while submitting contacts.';
        }
      });
  }

  clearContacts() {
    this.contactSet.clear();
    this.fileName = '';
    this.message = 'All imported contacts cleared';
    this.status = 'success';
  }

  get contactCount() {
    return this.contactSet.size;
  }
}
