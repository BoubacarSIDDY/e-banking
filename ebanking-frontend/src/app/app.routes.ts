import { Routes } from '@angular/router';
import { Customers } from "./customers/customers"
import { Accounts } from "./accounts/accounts"
import {NewCustomerComponent} from './new-customer/new-customer.component';
import {CustomerAccountsComponent} from './customer-accounts/customer-accounts.component';
export const routes: Routes = [
  {path : "customers", component : Customers},
  {path : "new-customer", component : NewCustomerComponent},
  {path : "accounts", component : Accounts},
  {path : "customer-accounts/:id", component : CustomerAccountsComponent}
];
