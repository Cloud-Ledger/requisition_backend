package cloudledger.requisition_system.service.service_impl;

import cloudledger.requisition_system.models.dao.AccountSources;
import cloudledger.requisition_system.models.dao.Requisitions;
import cloudledger.requisition_system.models.dto.request.AccountSourceRequest;
import cloudledger.requisition_system.models.dto.response.GetRequisitionDTO;
import cloudledger.requisition_system.models.dto.response.SourcesDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.AccountSourcesRepo;
import cloudledger.requisition_system.service.AccountSourceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountSourceImpl implements AccountSourceService {
    private final AccountSourcesRepo accountSourcesRepo;
    @Override
    public TheResponse createSource(AccountSourceRequest accountSourceRequest) {
        AccountSources sources = AccountSources.builder()
                .accountNumber(accountSourceRequest.getAccountNumber())
                .balance(accountSourceRequest.getBalance())
                .name(accountSourceRequest.getName())
                .currency(accountSourceRequest.getCurrency())
                .build();
        accountSourcesRepo.save(sources);
        return TheResponse.builder()
                .message("Account source saved successfully")
                .build();
    }

    @Override
    public List<SourcesDTO> getAllSources() {
        Iterable<AccountSources> all = accountSourcesRepo.findAll();
        List<SourcesDTO> allReqs = new ArrayList<>();
        all.iterator().forEachRemaining(u->{
            SourcesDTO sourcesResponse = SourcesDTO.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .accountNumber(u.getAccountNumber())
                    .balance(u.getBalance())
                    .currency(u.getCurrency())
                    .build();
            allReqs.add(sourcesResponse);
        });
        return allReqs;
    }
}
