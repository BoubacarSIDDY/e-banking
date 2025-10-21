import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CustomerModel} from '../model/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  backendHost: string = 'http://localhost:8085';
  // private backendHost = inject('backendHost');
  constructor(private http: HttpClient) {}

  public getCustomers() : Observable<Array<CustomerModel>>{
    return this.http.get<Array<CustomerModel>>(this.backendHost  + "/customers");
  }
  public searchCustomers(keyword:string) : Observable<Array<CustomerModel>>{
    return this.http.get<Array<CustomerModel>>(this.backendHost + "/customers/search?keyword=" + keyword);
  }
  public saveCustomer(customer:CustomerModel) : Observable<CustomerModel>{
    return this.http.post<CustomerModel>(this.backendHost + "/customers",customer);
  }
  public deleteCustomer(id:number){
    return this.http.delete(this.backendHost + "/customers"+id);
  }
}
