package cloudledger.requisition_system.controller;


import cloudledger.requisition_system.models.dao.Attachments;
import cloudledger.requisition_system.models.dao.Requisitions;
import cloudledger.requisition_system.models.dto.request.RequisitionDTO;
import cloudledger.requisition_system.models.dto.response.AttachmentsDTO;
import cloudledger.requisition_system.models.dto.response.GetRequisitionDTO;
import cloudledger.requisition_system.models.dto.response.SourcesDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.AttachmentsRepo;
import cloudledger.requisition_system.repository.RequisitionRepo;
import cloudledger.requisition_system.service.RequisitionService;
import cloudledger.requisition_system.utils.Response;
import cloudledger.requisition_system.utils.ResponseBuild;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/requisitions")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class RequisitionController {

    private final RequisitionService requisitionService;
    private final ResponseBuild<List<GetRequisitionDTO>> listRequisitionsResponseBuild;
    private final RequisitionRepo requisitionRepo;
    private final AttachmentsRepo attachmentsRepo;
    // Create a new requisition
    @PostMapping("/save/{id}/{email}")
    public ResponseEntity<TheResponse> createRequisition(@PathVariable(value = "id") Long id, @PathVariable(value = "email") String email, @RequestBody RequisitionDTO requisitionDTO) {
        TheResponse createdRequisition = requisitionService.createRequisition(id,email,requisitionDTO);
        return new ResponseEntity<>(createdRequisition, HttpStatus.CREATED);
    }

    // Get all requisitions
    @GetMapping
    public ResponseEntity<Response> getAllRequisitions() {
        return  new ResponseEntity<>(listRequisitionsResponseBuild.successResponse
                .apply(requisitionService.getAllRequisitions(),null), HttpStatus.CREATED);
    }
    @GetMapping("/filter")
    public ResponseEntity<Response> filterRequisitions(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String requisitionNumber,
            @RequestParam(required = false) String bankReference,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String beneficiary) {

        try{
            List<GetRequisitionDTO> requisitions = requisitionService.filterRequisitions(source, requisitionNumber, bankReference, amount, beneficiary);
            return  new ResponseEntity<>(listRequisitionsResponseBuild.successResponse
                    .apply(requisitions,null), HttpStatus.CREATED);
//            return ResponseEntity.ok(requisitions);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    // Update a requisition
    @PutMapping("/{id}")
    public ResponseEntity<RequisitionDTO> updateRequisition(
            @PathVariable Long id,
            @RequestBody RequisitionDTO requisitionDTO
    ) {
        RequisitionDTO updatedRequisition = requisitionService.updateRequisition(id, requisitionDTO);
        return new ResponseEntity<>(updatedRequisition, HttpStatus.OK);
    }

    // Delete a requisition
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequisition(@PathVariable Long id) {
        requisitionService.deleteRequisition(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/requisitions/{requisitionId}/attachments")
    public ResponseEntity<List<String>> getAllAttachments(@PathVariable Long requisitionId) {
        // Fetch requisition by ID
        Requisitions requisition = requisitionRepo.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Requisition not found"));

        // Fetch all attachments for the requisition
        List<AttachmentsDTO> attachments = attachmentsRepo.findByRequisition(requisition);

        // Generate the list of image URLs or paths
        List<String> imageUrls = attachments.stream()
                .map(attachment -> "/attachments/" + attachment.getId()) // Assuming your URLs are like this
                .collect(Collectors.toList());

        return ResponseEntity.ok(imageUrls); // Return list of image URLs
    }
}
