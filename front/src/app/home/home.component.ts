import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

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

  constructor(
    private contactService: ContactService,
    private authService: AuthService,
    private router: Router
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

}
