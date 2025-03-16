import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../auth.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  standalone: true,
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  // Use field names that match the backend model properties
  user = {
    nom: '',           // Instead of firstName
    prenom: '',        // Instead of lastName
    email: '',
    motDePasse: ''     // Instead of password
  };
  message = '';

  constructor(private authService: AuthService, private router: Router) {}

  async register() {
    try {
      // Log the data before sending to backend for debugging
      console.log('Sending registration data:', JSON.stringify(this.user));

      const response = await firstValueFrom(this.authService.register(this.user));
      if (response.status === 'success') {
        this.router.navigate(['/']);
        this.message = "Registration successful!";
      } else {
        this.message = response.message || "Registration failed";
      }
    } catch (error) {
      console.error('Registration error:', error);
      this.message = "Request failed. Please try again.";
    }
  }
}
