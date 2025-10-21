import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CustomerModel} from '../model/customer.model';
import {CustomerService} from '../services/customer.service';
import {throwError} from 'rxjs';
import {Router} from '@angular/router';

@Component({
  selector: 'app-new-customer',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {

    newCustomerForm!: FormGroup;

    constructor(private fb : FormBuilder, private customerService : CustomerService, private router : Router ) {
    }
    ngOnInit(): void {
        this.fb.group({
          name : this.fb.control(null, [Validators.required,Validators.minLength(4)]),
          email : this.fb.control(null,[Validators.required,Validators.email]),
        })
    }

    handleSaveCustomer() {
      let customer:CustomerModel = this.newCustomerForm.value;
      this.customerService.saveCustomer(customer).subscribe({
        next: ()=> {
          alert("Customer has been successfully saved");
          this.newCustomerForm.reset();
          this.router.navigateByUrl('/customers');
        },
        error: err => {
          console.log(err);
          return throwError(err);
        }
      })
    }
}
