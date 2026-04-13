package cloudledger.requisition_system.models.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_sources")
public class AccountSources {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Column(nullable = false)
        private String name;

        @NotBlank
        @Column(nullable = false, unique = true)
        private String accountNumber;

        @NotBlank
        @Column(nullable = false)
        private String currency;

        @Column(nullable = false)
        private Integer balance;

        @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Requisitions> requisitions;

}
