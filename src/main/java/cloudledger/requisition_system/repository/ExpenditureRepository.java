package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure,Long> {
}
