package sn.isi.ebanking_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.isi.ebanking_backend.entities.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where c.name like :kw")
    List<Customer> searchCustomers(@Param("kw") String keyword);
    List<Customer> findByNameContains(String keyword);
}
