package sn.isi.ebanking_backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.isi.ebanking_backend.entities.BankAccount;
import sn.isi.ebanking_backend.entities.CurrentAccount;
import sn.isi.ebanking_backend.entities.Customer;
import sn.isi.ebanking_backend.entities.SavingAccount;
import sn.isi.ebanking_backend.exceptions.BalanceNotSufficentException;
import sn.isi.ebanking_backend.exceptions.BankAccountNotFoundException;
import sn.isi.ebanking_backend.exceptions.CustomerNotFoundException;
import sn.isi.ebanking_backend.repositories.AccountOperationRepository;
import sn.isi.ebanking_backend.repositories.BankAccountRepository;
import sn.isi.ebanking_backend.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
//    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    // Injection de dépendance via le constructeur
//    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
//        this.customerRepository = customerRepository;
//        this.bankAccountRepository = bankAccountRepository;
//        this.accountOperationRepository = accountOperationRepository;
//    }

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new Customer");
        return customerRepository.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount=getBankAccount(accountId);
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficentException("Balance not sufficient");
        double newBalance = bankAccount.getBalance() - amount;
        bankAccount.setBalance(newBalance);
    }

    @Override
    public void credit(String accountId, double amount, String description) {

    }

    @Override
    public void virement(String accountIdSource, String accountIdDestination, double amount) {

    }
}
