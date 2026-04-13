package cloudledger.requisition_system.models.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pop_attachments")
public class Attachments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String fileName;

    @NotBlank
    @Column(nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "requisition_id")
    private Requisitions requisition;

    @ManyToOne
    @JoinColumn(name = "expenditure_id")
    private Expenditure expenditure;

    @ManyToOne
    @JoinColumn(name = "income_id")
    private Income income;
}
