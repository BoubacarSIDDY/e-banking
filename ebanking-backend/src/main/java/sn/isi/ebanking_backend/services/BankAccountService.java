package sn.isi.ebanking_backend.services;

import sn.isi.ebanking_backend.entities.BankAccount;
import sn.isi.ebanking_backend.entities.CurrentAccount;
import sn.isi.ebanking_backend.entities.Customer;
import sn.isi.ebanking_backend.entities.SavingAccount;
import sn.isi.ebanking_backend.exceptions.BalanceNotSufficentException;
import sn.isi.ebanking_backend.exceptions.BankAccountNotFoundException;
import sn.isi.ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String description);
    void virement(String accountIdSource, String accountIdDestination, double amount);
}
