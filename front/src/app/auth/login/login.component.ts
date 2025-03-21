import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../auth.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user = {
    email: '',
    motDePasse: ''
  };
  message = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  async login() {
    this.loading = true;
    try {
      console.log('Attempting login with:', this.user.email);
      const response = await firstValueFrom(this.authService.login(this.user));



      if (response.status === 'success') {

        localStorage.setItem('session', 'active');
        localStorage.setItem('nom',response.nom);
        localStorage.setItem('prenom',response.prenom);

        this.router.navigate(['/home']);
        this.message = '';
      } else if (response.status === 'error') {

        this.message = response.message;

      }
    } catch (error: any) {
      console.error('Login error:', error);
      this.message = error.error?.message || 'Invalid email or password';
    } finally {
      this.loading = false;
    }
  }
}
