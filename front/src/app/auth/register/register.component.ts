import { Component } from '@angular/core';
import { CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import { AuthService} from '../../auth.service';
import {firstValueFrom} from 'rxjs';


@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  standalone: true,
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  user = {email: '',password: '',nom: '',prenom: ''};
  message = '';

  constructor(private authService: AuthService, private router: Router) {
  }

  async register() {
    try {
      const resonse = await firstValueFrom(this.authService.register(this.user));
      if (resonse.status === 'success') {
        this.router.navigate(['/']);
        this.message = "success";
      } else {
        this.message = "error de enregistrement";
      }
    } catch (error) {
      this.message = "error de la demande";
    }
  }

}
