package sn.isi.ebanking_backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data @NoArgsConstructor
public class Customer {
    private Long id;
    private String name;
    private String email;
}
