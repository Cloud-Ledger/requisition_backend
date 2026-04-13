package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.Requisitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequisitionRepo extends JpaRepository<Requisitions, Long> {
    Optional<Requisitions> findByRequisitionNumber(String requisitionNumber);
    @Query("SELECT r FROM Requisitions r WHERE " +
            "(:source IS NULL OR r.source.name LIKE %:source%) AND " +
            "(:requisitionNumber IS NULL OR r.requisitionNumber LIKE %:requisitionNumber%) AND " +
            "(:bankReference IS NULL OR r.bankReference LIKE %:bankReference%) AND " +
            "(:amount IS NULL OR r.amount = :amount) AND " +
            "(:beneficiary IS NULL OR r.beneficiary LIKE %:beneficiary%)")
    List<Requisitions> filterRequisitions(
            @Param("source") String source,
            @Param("requisitionNumber") String requisitionNumber,
            @Param("bankReference") String bankReference,
            @Param("amount") Double amount,
            @Param("beneficiary") String beneficiary);

}
