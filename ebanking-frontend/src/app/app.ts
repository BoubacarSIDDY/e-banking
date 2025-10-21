import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Navbar} from "./navbar/navbar";
import {ReactiveFormsModule} from '@angular/forms';
@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    Navbar,
    ReactiveFormsModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ebanking-frontend');
}
