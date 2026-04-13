package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.AccountSources;
import cloudledger.requisition_system.models.dao.Requisitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSourcesRepo extends JpaRepository<AccountSources, Long> {
    Optional<AccountSources> findAccountSourcesById(Long id);

}
