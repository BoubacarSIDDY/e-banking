package sn.isi.ebanking_backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sn.isi.ebanking_backend.dtos.*;
import sn.isi.ebanking_backend.entities.*;
import sn.isi.ebanking_backend.enums.AccountStatus;
import sn.isi.ebanking_backend.enums.OperationType;
import sn.isi.ebanking_backend.exceptions.BalanceNotSufficentException;
import sn.isi.ebanking_backend.exceptions.BankAccountNotFoundException;
import sn.isi.ebanking_backend.exceptions.CustomerNotFoundException;
import sn.isi.ebanking_backend.mappers.BankAccountMapperImpl;
import sn.isi.ebanking_backend.repositories.AccountOperationRepository;
import sn.isi.ebanking_backend.repositories.BankAccountRepository;
import sn.isi.ebanking_backend.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;
//    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    // Injection de dépendance via le constructeur
//    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
//        this.customerRepository = customerRepository;
//        this.bankAccountRepository = bankAccountRepository;
//        this.accountOperationRepository = accountOperationRepository;
//    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.customerDtoToCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.customerToCustomerDTO(savedCustomer);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCurrency("XOF");
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedCurrent = bankAccountRepository.save(currentAccount);
        return dtoMapper.toCurrentAccountDTO(savedCurrent);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCurrency("XOF");
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedSaving = bankAccountRepository.save(savingAccount);
        return dtoMapper.toSavingAccountDTO(savedSaving);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        // Programmation fonctionnelle
        List<CustomerDTO> customerDTOList = customers.stream()
                .map(cust -> dtoMapper.customerToCustomerDTO(cust))
                .collect(Collectors.toList());
        // Programmation fonctionelle
//        List<CustomerDTO> customerDTOList = new ArrayList<>();
//        for (Customer customer : customers){
//            CustomerDTO customerDTO = dtoMapper.customerToCustomerDTO(customer);
//            customerDTOList.add(customerDTO);
//        }
        return customerDTOList;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found !"));
        return dtoMapper.customerToCustomerDTO(customer);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount instanceof CurrentAccount){
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.toCurrentAccountDTO(currentAccount);
        }else {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.toSavingAccountDTO(savingAccount);
        }
    }

    private BankAccount getBankAccountById(String accountId) throws BankAccountNotFoundException {
        return  bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount=getBankAccountById(accountId);
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficentException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccountById(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
    }

    @Override
    public void virement(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount,"Transfer to " + accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from " + accountIdSource);
    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOList = bankAccountList.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount currentAccount) {
                return dtoMapper.toCurrentAccountDTO(currentAccount);
            } else {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.toSavingAccountDTO(savingAccount);
            }
        }).toList();
        return bankAccountDTOList;
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO){
        log.info("Updating customer");
        Customer customer = dtoMapper.customerDtoToCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.customerToCustomerDTO(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List <AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().
                map(accountOperation -> dtoMapper.toAccountOperationDTO(accountOperation))
                .toList();
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int currentPage, int pageSize) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(accountId);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(currentPage, pageSize));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> listOperationsDTO = accountOperations.getContent()
                .stream()
                .map(accountOperation -> dtoMapper.toAccountOperationDTO(accountOperation))
                .toList();
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(pageSize);
        accountHistoryDTO.setCurrentPage(currentPage);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setAccountOperationDTOS(listOperationsDTO);
        return accountHistoryDTO;
    }
}
