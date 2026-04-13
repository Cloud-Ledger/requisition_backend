package cloudledger.requisition_system.controller;

import cloudledger.requisition_system.models.dao.Attachments;
import cloudledger.requisition_system.models.dto.response.AttachmentsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.AttachmentsRepo;
import cloudledger.requisition_system.service.service_impl.AttachmentServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/attachments")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class AttachmentsController {
    private final AttachmentServiceImpl attachmentService;
    private final AttachmentsRepo attachmentsRepo;
    @PostMapping(path = "/payments/{requisitionId}", consumes = "multipart/form-data")
    public ResponseEntity<TheResponse> uploadPaymentAttachment(@PathVariable Long requisitionId,
                                                        @RequestParam("file") MultipartFile file) {
        try {
            TheResponse saveAttachment = attachmentService.saveRequisitionAttachment(requisitionId, file);
            return new ResponseEntity<>(saveAttachment, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping(path = "/incomes/{incomeId}", consumes = "multipart/form-data")
    public ResponseEntity<TheResponse> uploadIncomeAttachment(@PathVariable Long incomeId, @RequestParam("file") MultipartFile file) {
        try {
            TheResponse saveAttachment = attachmentService.saveIncomeAttachment(incomeId, file);
            return new ResponseEntity<>(saveAttachment, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping(path = "/expenditures/{expenditureId}", consumes = "multipart/form-data")
    public ResponseEntity<TheResponse> uploadExpenditureAttachment(@PathVariable Long expenditureId, @RequestParam("file") MultipartFile file) {
        try {
            TheResponse saveAttachment = attachmentService.saveExpenditureAttachment(expenditureId, file);
            return new ResponseEntity<>(saveAttachment, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<Resource> getAttachment(@PathVariable Long attachmentId) {
        Attachments attachment = attachmentsRepo.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        Path filePath = Paths.get(attachment.getFilePath());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        String contentType = "image/jpeg"; // or dynamically detect based on the file extension
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/all")

    public ResponseEntity<List<AttachmentsDTO>> getAllAttachments() {
        List<AttachmentsDTO> attachments = attachmentService.getAllAttachments();
        return ResponseEntity.ok(attachments);
    }


}
