package cloudledger.requisition_system.controller;


import cloudledger.requisition_system.models.dao.Expenditure;
import cloudledger.requisition_system.models.dao.FinReport;
import cloudledger.requisition_system.models.dao.Income;
import cloudledger.requisition_system.models.dto.request.ExpenditureDTO;
import cloudledger.requisition_system.models.dto.request.FinReportDTO;
import cloudledger.requisition_system.models.dto.request.IncomeDTO;
import cloudledger.requisition_system.models.dto.response.GetFinReportsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.FinReportRepository;
import cloudledger.requisition_system.service.FinReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/reports")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class FinReportController {

    private final FinReportService finReportService;
    private final FinReportRepository finReportRepository;

    @PostMapping("/save/{id}")
    public ResponseEntity<TheResponse> saveFinanceRecord(@PathVariable(value = "id") Long id, @RequestBody FinReportDTO financeRecordDTO) {
        TheResponse savedRecord = finReportService.saveFinanceRecord(id,financeRecordDTO);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetFinReportsDTO>> getAllFinanceRecords() {
        return ResponseEntity.ok(finReportService.getAllFinanceRecords());
    }
    @GetMapping("/project/{id}")
    public ResponseEntity<List<GetFinReportsDTO>> getReportsByProject(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(finReportService.getFinanceRecordsByProject(id));
    }

    @PutMapping("/income/{id}")
    public ResponseEntity<TheResponse> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO incomeDTO) {
        return ResponseEntity.ok(finReportService.updateIncome(id, incomeDTO));
    }

    @PutMapping("/expenditure/{id}")
    public ResponseEntity<TheResponse> updateExpenditure(@PathVariable Long id, @RequestBody ExpenditureDTO expenditureDTO) {
        return ResponseEntity.ok(finReportService.updateExpenditure(id, expenditureDTO));
    }

    @PatchMapping("/update/{id}")
    public TheResponse updateFinReport(@PathVariable Long id, @RequestBody FinReportDTO updatedReport) {
        Optional<FinReport> existingReport = finReportRepository.findById(id);
        if (existingReport.isPresent()) {
            FinReport report = existingReport.get();

            // Add new incomes to the existing incomes list
            updatedReport.getIncomes().forEach(incomeDTO -> {
                Income income = Income.builder()
                        .date(incomeDTO.getDate())
                        .amount(incomeDTO.getAmount())
                        .bankReference(incomeDTO.getBankReference())
                        .briefReason(incomeDTO.getBriefReason())
                        .finReport(report)
                        .build();
                report.getIncomes().add(income);
            });

            // Add new expenditures to the existing expenditures list
            updatedReport.getExpenditures().forEach(expenditureDTO -> {
                Expenditure expenditure = Expenditure.builder()
                        .date(expenditureDTO.getDate())
                        .amount(expenditureDTO.getAmount())
                        .bankReference(expenditureDTO.getBankReference())
                        .briefReason(expenditureDTO.getBriefReason())
                        .finReport(report)
                        .build();
                report.getExpenditures().add(expenditure);
            });

            // Save the updated report
            finReportRepository.save(report);
            return TheResponse.builder()
                    .message("Report successfully updated")
                    .build();
        } else {
            return TheResponse.builder()
                    .message("Failed to updated")
                    .build();
        }
    }

}
