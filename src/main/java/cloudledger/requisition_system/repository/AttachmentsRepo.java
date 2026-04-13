package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.Attachments;
import cloudledger.requisition_system.models.dao.Requisitions;
import cloudledger.requisition_system.models.dto.response.AttachmentsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentsRepo extends JpaRepository<Attachments, Long> {
    List<AttachmentsDTO> findByRequisition(Requisitions requisition);

}
