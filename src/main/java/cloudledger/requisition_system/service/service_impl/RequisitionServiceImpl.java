package cloudledger.requisition_system.service.service_impl;

import cloudledger.requisition_system.exceptions.RunTimeExceptionPlaceHolder;
import cloudledger.requisition_system.models.dao.AccountSources;
import cloudledger.requisition_system.models.dao.Requisitions;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.request.RequisitionDTO;
import cloudledger.requisition_system.models.dto.response.GetRequisitionDTO;
import cloudledger.requisition_system.models.dto.response.GetUserDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.AccountSourcesRepo;
import cloudledger.requisition_system.repository.RequisitionRepo;
import cloudledger.requisition_system.repository.UserRepository;
import cloudledger.requisition_system.service.RequisitionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RequisitionServiceImpl implements RequisitionService {
    private final UserRepository userRepository;
    private final RequisitionRepo requisitionRepo;
    private final AccountSourcesRepo accountSourcesRepo;
    @Override
    public TheResponse createRequisition(Long id, String email, RequisitionDTO requisitionDTO) {
        Optional<User> user = userRepository.findByEmail(email);
        User userByUserName = user.orElseThrow(() ->
                new RunTimeExceptionPlaceHolder("Username doesn't exist!!")
        );
        Optional<AccountSources> sources = accountSourcesRepo.findAccountSourcesById(id);
        AccountSources source = sources.orElseThrow(() ->
                new RunTimeExceptionPlaceHolder("Source doesn't exist!!")
        );

        Requisitions requisitions = Requisitions.builder()
                    .amount(requisitionDTO.getAmount())
                    .requisitionNumber(requisitionDTO.getRequisitionNumber())
                    .bankReference(requisitionDTO.getBankReference())
                    .beneficiary(requisitionDTO.getBeneficiary())
                    .briefReason(requisitionDTO.getBriefReason())
                    .date(requisitionDTO.getDate())
                    .source(source)
                    .status(requisitionDTO.getStatus())
                    .createdBy(userByUserName)
                    .build();
        requisitionRepo.save(requisitions);
        return TheResponse.builder()
                    .message("Requisition Saved Successfully")
                    .statusCode("200")
                    .build();
        }



    @Override
    public RequisitionDTO updateRequisition(Long id, RequisitionDTO requisitionDTO) {
        return null;
    }

    @Override
    public GetRequisitionDTO getRequisitionById(Long id) {
        return null;
    }

    @Override
    public List<GetRequisitionDTO> getAllRequisitions() {
        Iterable<Requisitions> all = requisitionRepo.findAll();
        List<GetRequisitionDTO> allReqs = new ArrayList<>();
        all.iterator().forEachRemaining(u->{
            GetRequisitionDTO requisitionResponse = GetRequisitionDTO.builder()
                    .id(u.getId())
                    .amount(u.getAmount())
                    .requisitionNumber(u.getRequisitionNumber())
                    .bankReference(u.getBankReference())
                    .beneficiary(u.getBeneficiary())
                    .briefReason(u.getBriefReason())
                    .date(u.getDate())
                    .status(u.getStatus())
                    .userEmail(u.getCreatedBy().getEmail())
                    .source(u.getSource().getName())
                    .build();
            allReqs.add(requisitionResponse);
        });
        return allReqs;
    }

    @Override
    public List<GetRequisitionDTO> filterRequisitions(String source, String requisitionNumber, String bankReference, Double amount, String beneficiary) {
        Iterable<Requisitions> all = requisitionRepo.filterRequisitions(source, requisitionNumber, bankReference, amount, beneficiary);
        List<GetRequisitionDTO> allReqs = new ArrayList<>();
        all.iterator().forEachRemaining(u->{
            GetRequisitionDTO requisitionResponse = GetRequisitionDTO.builder()
                    .id(u.getId())
                    .amount(u.getAmount())
                    .requisitionNumber(u.getRequisitionNumber())
                    .bankReference(u.getBankReference())
                    .beneficiary(u.getBeneficiary())
                    .briefReason(u.getBriefReason())
                    .date(u.getDate())
                    .status(u.getStatus())
                    .userEmail(u.getCreatedBy().getEmail())
                    .source(u.getSource().getName())
                    .build();
            allReqs.add(requisitionResponse);
        });
        return allReqs;
    }


    @Override
    public void deleteRequisition(Long id) {

    }
}
