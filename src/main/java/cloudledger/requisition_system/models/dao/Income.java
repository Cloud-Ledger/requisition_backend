package cloudledger.requisition_system.models.dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Integer amount;
    private String bankReference;
    private String briefReason;

    @ManyToOne
    @JoinColumn(name = "fin_report_id")
    private FinReport finReport;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "income", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachments> attachments;

}
