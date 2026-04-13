package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.AccountSources;
import cloudledger.requisition_system.models.dao.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findProjectById(Long id);
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Integer userId);
}
