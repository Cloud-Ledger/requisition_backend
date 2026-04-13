package cloudledger.requisition_system.service.service_impl;

import cloudledger.requisition_system.models.dao.*;
import cloudledger.requisition_system.models.dto.response.AttachmentsDTO;
import cloudledger.requisition_system.models.dto.response.GetFinReportsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.AttachmentsRepo;
import cloudledger.requisition_system.repository.ExpenditureRepository;
import cloudledger.requisition_system.repository.IncomeRepository;
import cloudledger.requisition_system.repository.RequisitionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl {
    private static final String STORAGE_DIR = "attachments/";
    private final AttachmentsRepo attachmentsRepo;
    private final RequisitionRepo requisitionRepo;
    private final IncomeRepository incomeRepository;
    private final ExpenditureRepository expenditureRepository;

    public TheResponse saveRequisitionAttachment(Long requisitionId, MultipartFile file) throws IOException {
        Requisitions requisition = requisitionRepo.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Requisition not found"));

        // Save file to local storage
        Path storagePath = Paths.get(STORAGE_DIR + file.getOriginalFilename());
        Files.createDirectories(storagePath.getParent());
        Files.write(storagePath, file.getBytes());

        // Save attachment metadata
        Attachments attachment = new Attachments();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(storagePath.toString());
        attachment.setRequisition(requisition);

        attachmentsRepo.save(attachment);
        return TheResponse.builder()
                .message("Proof of Payment Saved Successfully")
                .statusCode("200")
                .build();
    }
    public TheResponse saveIncomeAttachment(Long incomeId, MultipartFile file) throws IOException {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new IllegalArgumentException("Income report not found"));

        // Save file to local storage
        Path storagePath = Paths.get(STORAGE_DIR + file.getOriginalFilename());
        Files.createDirectories(storagePath.getParent());
        Files.write(storagePath, file.getBytes());

        // Save attachment metadata
        Attachments attachment = new Attachments();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(storagePath.toString());
        attachment.setIncome(income);

        attachmentsRepo.save(attachment);
        return TheResponse.builder()
                .message("Attachment Saved Successfully")
                .statusCode("200")
                .build();
    }
    public TheResponse saveExpenditureAttachment(Long expenditureId, MultipartFile file) throws IOException {
        Expenditure expenditure = expenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new IllegalArgumentException("Expenditure report not found"));

        // Save file to local storage
        Path storagePath = Paths.get(STORAGE_DIR + file.getOriginalFilename());
        Files.createDirectories(storagePath.getParent());
        Files.write(storagePath, file.getBytes());

        // Save attachment metadata
        Attachments attachment = new Attachments();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(storagePath.toString());
        attachment.setExpenditure(expenditure);

        attachmentsRepo.save(attachment);
        return TheResponse.builder()
                .message("Attachment Saved Successfully")
                .statusCode("200")
                .build();
    }


    public List<AttachmentsDTO> getAllAttachmentsByRequisitionId(Long requisitionId) {
        Requisitions requisition = requisitionRepo.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Requisition not found"));

        return attachmentsRepo.findByRequisition(requisition);

    }
    public List<AttachmentsDTO> getAllAttachments() {
        Iterable<Attachments> all = attachmentsRepo.findAll();
        List<AttachmentsDTO> allReps = new ArrayList<>();

        all.forEach(u -> {
            AttachmentsDTO requisitionResponse = AttachmentsDTO.builder()
                    .id(String.valueOf(u.getId()))
                    .name(u.getFileName())
                    .path(u.getFilePath())
                    .requisitionNumber(u.getRequisition() != null ? u.getRequisition().getRequisitionNumber() : null)
                    .expReference(u.getExpenditure() != null ? u.getExpenditure().getBankReference() : null)
                    .incReference(u.getIncome() != null ? u.getIncome().getBankReference() : null)
                    .reqDate(u.getRequisition() != null ? u.getRequisition().getDate() : null)
                    .expDate(u.getExpenditure() != null ? u.getExpenditure().getDate() : null)
                    .incDate(u.getIncome() != null ? u.getIncome().getDate() : null)
                    .build();
            allReps.add(requisitionResponse);
        });

        return allReps;
    }

}
