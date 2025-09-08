package sn.isi.ebanking_backend.dtos;

import lombok.Data;
import sn.isi.ebanking_backend.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;
}
