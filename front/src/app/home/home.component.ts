import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  user: { nom: string, prenom: string } | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {

    this.user = this.authService.getUserDetails();

  }

  logout() {
    this.authService.logout().subscribe();
  }
  addcontact(){
    this.router.navigate(['/contact/add-contact'])
  }

}
