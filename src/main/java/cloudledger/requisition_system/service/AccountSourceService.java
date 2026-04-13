package cloudledger.requisition_system.service;

import cloudledger.requisition_system.models.dto.request.AccountSourceRequest;
import cloudledger.requisition_system.models.dto.response.GetRequisitionDTO;
import cloudledger.requisition_system.models.dto.response.SourcesDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;

import java.util.List;

public interface AccountSourceService {
    TheResponse createSource(AccountSourceRequest accountSourceRequest);
    List<SourcesDTO> getAllSources();

}
