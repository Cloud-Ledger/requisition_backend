package cloudledger.requisition_system.service;

import cloudledger.requisition_system.models.dto.request.RequisitionDTO;
import cloudledger.requisition_system.models.dto.response.GetRequisitionDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;

import java.util.List;

public interface RequisitionService {
    TheResponse createRequisition(Long id, String email, RequisitionDTO requisitionDTO);
    RequisitionDTO updateRequisition(Long id, RequisitionDTO requisitionDTO);
    GetRequisitionDTO getRequisitionById(Long id);
    List<GetRequisitionDTO> getAllRequisitions();
    List<GetRequisitionDTO> filterRequisitions(String source, String requisitionNumber, String bankReference, Double amount, String beneficiary);
    void deleteRequisition(Long id);
}
