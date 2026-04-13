package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}
