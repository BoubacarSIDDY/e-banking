import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JsonPipe, NgForOf, NgIf} from '@angular/common';
import {Customer} from '../services/customer';

@Component({
  selector: 'app-customers',
  imports: [
    NgForOf,
    NgIf,
    JsonPipe
  ],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {
  customers : any;
  errorMessage ?: string;
  constructor(private customerService : Customer) {
  }

  ngOnInit(): void {
    this.customerService.getCustomers().subscribe({
      next: data => {
        this.customers = data;
      },
      error: error => {
        this.errorMessage = error;
      }
    })
  }
}
