package cloudledger.requisition_system.models.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<FinReport> financeRecords;

    @ManyToMany(mappedBy = "assignedProjects")
    private List<User> users;

}

