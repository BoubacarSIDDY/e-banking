package sn.isi.ebanking_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.isi.ebanking_backend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
