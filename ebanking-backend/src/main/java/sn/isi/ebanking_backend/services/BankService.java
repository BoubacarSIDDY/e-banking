package sn.isi.ebanking_backend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.isi.ebanking_backend.entities.BankAccount;
import sn.isi.ebanking_backend.entities.CurrentAccount;
import sn.isi.ebanking_backend.entities.SavingAccount;
import sn.isi.ebanking_backend.repositories.BankAccountRepository;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("1fe8a7bb-2b1a-4c7c-ab0f-00829131a1b8").orElse(null);
        System.out.println("********************");
        if (bankAccount != null){
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println("Date => "+bankAccount.getCreatedAt());
            System.out.println("Client => " + bankAccount.getCustomer().getName());
            System.out.println("Type compte => " + bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount){
                System.out.println("Over draft => " + ((CurrentAccount) bankAccount).getOverDraft());
            }else if(bankAccount instanceof SavingAccount){
                System.out.println("Rate =>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(accountOperation -> {
                System.out.println("========================");
                System.out.println(accountOperation.getType()+"\t : "+accountOperation.getOperationDate()+"\t : "+accountOperation.getAmount());
            });
        }
    }
}
