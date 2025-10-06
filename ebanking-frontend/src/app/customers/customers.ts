import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-customers',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {
  customers : any;
  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get('http://localhost:8085/customers').subscribe(data=>{
      this.customers = data;
    }, error => console.log(error));
  }
}
