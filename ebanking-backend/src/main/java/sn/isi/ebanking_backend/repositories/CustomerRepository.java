package sn.isi.ebanking_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.isi.ebanking_backend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
