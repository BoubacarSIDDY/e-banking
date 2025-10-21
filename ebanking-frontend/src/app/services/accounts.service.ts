import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';
import {AccountDetailsModel} from '../model/account.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {
  constructor(private http: HttpClient) {}

  public getAccount(accountId:string, page:number,size:number):Observable<AccountDetailsModel>{
    return this.http.get<AccountDetailsModel>(environment.apiUrl+'accounts/'+accountId+'/pageOperations?page='+page+'&size='+size);
  }
  public debit(accountId:string, amount:number,description:string){
    let data = {accountId:accountId,amount:amount,description:description};
    return this.http.post(environment.apiUrl+'accounts/debit',data);
  }
  public credit(accountId:string, amount:number,description:string){
    let data = {accountId:accountId,amount:amount,description:description};
    return this.http.post(environment.apiUrl+'accounts/credit',data);
  }
  public transfer(accountSource:string, accountDestination:string,amount:number){
    let data = {accountSource,accountDestination,amount};
    return this.http.post(environment.apiUrl+'accounts/transfer',data);
  }
}
