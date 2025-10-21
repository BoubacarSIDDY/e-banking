import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AccountsService} from '../services/accounts.service';
import {catchError, Observable, throwError} from 'rxjs';
import {AccountDetailsModel} from '../model/account.model';
import {AsyncPipe, DatePipe, DecimalPipe, NgClass, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-accounts',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    AsyncPipe,
    DecimalPipe,
    NgForOf,
    DatePipe,
    NgClass
  ],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts implements OnInit{

    accountFormGroup!: FormGroup;
    currentPage : number = 0;
    sizePage : number = 5;
    accountObservable!: Observable<AccountDetailsModel>;
    operationFormGroup!: FormGroup;
    errorMessage : string = "";

    constructor(private formBuilder: FormBuilder,private accountService: AccountsService) {}

    ngOnInit(): void {
        this.accountFormGroup = this.formBuilder.group({
          accountId : this.formBuilder.control('', [Validators.required]),
        })
        this.operationFormGroup = this.formBuilder.group({
          operationType : this.formBuilder.control(null, [Validators.required]),
          amount : this.formBuilder.control(0, [Validators.required]),
          description : this.formBuilder.control(null, [Validators.required]),
          accountDestination : this.formBuilder.control(null, [Validators.required])
        })
    }

    handleSearchAccount() {
        let accountId : string = this.accountFormGroup.value.accountId;
        this.accountObservable = this.accountService.getAccount(accountId,this.currentPage,this.sizePage).pipe(
          catchError(err => {
            this.errorMessage = err.message;
            return throwError(err)
          })
        );
    }
    gotoPage(page: number) {
      this.currentPage = page;
      this.handleSearchAccount();
    }

    handleAccountOperation() {
      let accountId : string = this.accountFormGroup.value.accountId;
      let operationType : string = this.operationFormGroup.value.operationType;
      let accountDestination : string = this.operationFormGroup.value.accountDestination;
      let amount : number = this.operationFormGroup.value.amount;
      let description : string = this.operationFormGroup.value.description;
      if (operationType === 'DEBIT') {
        this.accountService.debit(accountId,amount,description).subscribe({
          next: () => {
            alert("Successfully debit");
            this.handleSearchAccount();
          },
          error: (error) => {
            console.log(error);
          }
        })
      }else if(operationType === 'CREDIT') {
        this.accountService.credit(accountId,amount,description).subscribe({
          next: () => {
            alert("Successfully credit");
            this.handleSearchAccount();
          },
          error: (error) => {
            console.log(error);
          }
        })
      }else if(operationType === 'TRANSFER') {
        this.accountService.transfer(accountId,accountDestination,amount).subscribe({
          next: () => {
            alert("Successfully transfer");
            this.handleSearchAccount();
          },
          error: (error) => {
            console.log(error);
          }
        })
      }
      this.operationFormGroup.reset();
    }
}
