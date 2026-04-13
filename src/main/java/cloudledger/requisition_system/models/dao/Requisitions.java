package cloudledger.requisition_system.models.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requisitions")
public class Requisitions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String requisitionNumber;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private AccountSources source;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String beneficiary;

    @Column(nullable = false)
    private Integer amount;

    private String bankReference;

    private String briefReason;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, PAID

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachments> attachments;
}
