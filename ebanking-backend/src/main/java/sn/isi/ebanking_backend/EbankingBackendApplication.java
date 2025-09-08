package sn.isi.ebanking_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sn.isi.ebanking_backend.dtos.BankAccountDTO;
import sn.isi.ebanking_backend.dtos.CurrentAccountDTO;
import sn.isi.ebanking_backend.dtos.CustomerDTO;
import sn.isi.ebanking_backend.dtos.SavingAccountDTO;
import sn.isi.ebanking_backend.entities.*;
import sn.isi.ebanking_backend.enums.AccountStatus;
import sn.isi.ebanking_backend.enums.OperationType;
import sn.isi.ebanking_backend.exceptions.BalanceNotSufficentException;
import sn.isi.ebanking_backend.exceptions.BankAccountNotFoundException;
import sn.isi.ebanking_backend.exceptions.CustomerNotFoundException;
import sn.isi.ebanking_backend.repositories.AccountOperationRepository;
import sn.isi.ebanking_backend.repositories.BankAccountRepository;
import sn.isi.ebanking_backend.repositories.CustomerRepository;
import sn.isi.ebanking_backend.services.BankAccountService;
import sn.isi.ebanking_backend.services.BankService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Oki","Hassan","Boubacar").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*15000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(3.5);
				savingAccount.setCustomer(customer);
				bankAccountRepository.save(savingAccount);
			});

			bankAccountRepository.findAll().forEach(bankAccount -> {
				for (int i = 0; i < 5; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(bankAccount);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}
//	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
			Stream.of("Hassan","Mohamed","Aboubakr").forEach(name->{
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setName(name);
				customerDTO.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customerDTO);
			});
			bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*150000,10000,customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*140000,6.5, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

			List<BankAccountDTO> bankAccountList = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount:bankAccountList){
				for (int i = 0; i < 10; i++) {
					String accountId;
					if (bankAccount instanceof SavingAccountDTO savingAccountDTO){
						accountId = savingAccountDTO.getId();
					}else {
						accountId = ((CurrentAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
					bankAccountService.debit(accountId,1000+Math.random()*10000,"Debit");
				}
			}
		};
	}
}
