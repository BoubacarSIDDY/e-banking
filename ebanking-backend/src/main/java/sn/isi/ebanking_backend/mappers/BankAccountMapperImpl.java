package sn.isi.ebanking_backend.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import sn.isi.ebanking_backend.dtos.AccountOperationDTO;
import sn.isi.ebanking_backend.dtos.CurrentAccountDTO;
import sn.isi.ebanking_backend.dtos.CustomerDTO;
import sn.isi.ebanking_backend.dtos.SavingAccountDTO;
import sn.isi.ebanking_backend.entities.AccountOperation;
import sn.isi.ebanking_backend.entities.CurrentAccount;
import sn.isi.ebanking_backend.entities.Customer;
import sn.isi.ebanking_backend.entities.SavingAccount;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO customerToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        // **** Methode 1 : Utilisation de BeanUtils : copie des propriétés dynamiquement sur souces vers destination
        BeanUtils.copyProperties(customer,customerDTO);
        // **** Methode 2 : Utilisation de setters
//        customerDTO.setId(customer.getId());
//        customerDTO.setName(customer.getName());
//        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public Customer customerDtoToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingAccountDTO toSavingAccountDTO(SavingAccount savingAccount){
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        savingAccountDTO.setCustomerDTO(customerToCustomerDTO(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    public SavingAccount toSavingAccount(SavingAccountDTO savingAccountDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO,savingAccount);
        savingAccount.setCustomer(customerDtoToCustomer(savingAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentAccountDTO toCurrentAccountDTO(CurrentAccount currentAccount){
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomerDTO(customerToCustomerDTO(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    public CurrentAccount toCurrentAccount(CurrentAccountDTO currentAccountDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO,currentAccount);
        currentAccount.setCustomer(customerDtoToCustomer(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO toAccountOperationDTO(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }
}
