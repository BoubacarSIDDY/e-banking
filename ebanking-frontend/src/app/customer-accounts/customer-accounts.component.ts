import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CustomerModel} from '../model/customer.model';

@Component({
  selector: 'app-customer-accounts',
  imports: [],
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css'
})
export class CustomerAccountsComponent implements OnInit {
    customerId!: string;
    customer!: CustomerModel;
    constructor(private route: ActivatedRoute, private router:Router) {
      this.customer = this.router.getCurrentNavigation()?.extras.state as CustomerModel;
    }
    ngOnInit(): void {
        this.customerId = this.route.snapshot.params['id'];
    }
    listCustomerAccounts(): void {

    }
}
