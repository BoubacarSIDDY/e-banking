package sn.isi.ebanking_backend.dtos;

import lombok.Data;
import sn.isi.ebanking_backend.enums.AccountStatus;
import java.util.Date;

@Data
public class CurrentAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private String currency;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
