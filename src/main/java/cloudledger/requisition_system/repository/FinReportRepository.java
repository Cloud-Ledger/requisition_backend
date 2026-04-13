package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.FinReport;
import cloudledger.requisition_system.models.dao.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinReportRepository extends JpaRepository<FinReport, Long> {
    List<FinReport> findByProjectId(Long projectId);

    Optional<FinReport> findByProject(Project project);
}
