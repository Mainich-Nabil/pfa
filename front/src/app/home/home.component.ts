import { Component } from '@angular/core';
import { CommonModule} from '@angular/common';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';



@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  standalone: true,
  styleUrl: './home.component.css'
})
export class HomeComponent {


  constructor(private authService: AuthService,private router: Router ) {
  }

  logout(){
    this.authService.logout().subscribe({
      next: () =>{
        console.log('logged out');
      },
      error: (err) => {

        this.router.navigate(['/login']);
      }

    });
  }

  user = {
    nom : localStorage.getItem('nom') || '',
    prenom: localStorage.getItem('prenom') || ''
  }


}
