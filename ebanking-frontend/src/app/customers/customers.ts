import {Component, inject, OnInit, signal} from '@angular/core';
import {AsyncPipe, JsonPipe, NgForOf, NgIf} from '@angular/common';
import {CustomerService} from '../services/customer.service';
import {CustomerModel} from '../model/customer.model';
import {catchError, filter, map, Observable, throwError} from 'rxjs';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-customers',
  imports: [
    NgForOf,
    NgIf,
    JsonPipe,
    AsyncPipe,
    ReactiveFormsModule
  ],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {

  customers$! : Observable<Array<CustomerModel>>;
  errorMessage ?: string;
  searchFormGroup! : FormGroup;

  constructor(private customerService : CustomerService, private formBuilder : FormBuilder, private router : Router) {
  }
  // customers$ = signal<CustomerModel[]>([]);
  // errorMessage = signal<string>('');
  // isLoading = signal<boolean>(false);
  // private customerService = inject(CustomerService);
  // private formBuilder = inject(FormBuilder);

  // searchFormGroup = this.formBuilder.group({
  //   keyword : this.formBuilder.control("")
  // });
  ngOnInit(): void {
    this.searchFormGroup = this.formBuilder.group({
      keyword : this.formBuilder.control("")
    });
    this.customers$ = this.loadCustomers();
    // this.customers$.set(this.loadCustomers());
  }

  loadCustomers(){
    // this.isLoading.set(true);
    // this.errorMessage.set("");
    return this.customerService.getCustomers().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

  handleSearchCustomers() {
    let kw = this.searchFormGroup.value.keyword;
    this.customers$ = this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  handleDeleteCustomer(customer: CustomerModel) {
    let conf = confirm("Are you sure?");
    if(!conf) return;
    this.customerService.deleteCustomer(customer.id).subscribe({
      next: ()=> {
        alert("Customer deleted");
        this.customers$ = this.customers$.pipe(
          map(data=>{
            let index = data.indexOf(customer);
            data.splice(index,1);
            return data;
          })
        )
      },
      error: (err)=> {
        console.log(err);
      }
    })
  }

  handleCustomerAccounts(customerModel: CustomerModel) {
    this.router.navigateByUrl("/customer-accounts/"+customerModel.id, {state:customerModel});
  }
}
