package cloudledger.requisition_system.models.dao;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "finReport",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Income> incomes;

    @OneToMany(mappedBy = "finReport",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expenditure> expenditures;

    @OneToMany(mappedBy = "finReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
