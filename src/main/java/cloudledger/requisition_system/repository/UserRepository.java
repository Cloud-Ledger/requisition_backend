package cloudledger.requisition_system.repository;

import java.util.Optional;

import cloudledger.requisition_system.models.dao.Role;
import cloudledger.requisition_system.models.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  User findUserByEmail(String email);
  User findUserById(Integer id);
  boolean existsByEmail(String email);
  boolean existsByRole(Role role);



}
