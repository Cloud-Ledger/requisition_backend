package cloudledger.requisition_system.service.service_impl;

import cloudledger.requisition_system.exceptions.RunTimeExceptionPlaceHolder;
import cloudledger.requisition_system.models.dao.*;
import cloudledger.requisition_system.models.dto.request.ExpenditureDTO;
import cloudledger.requisition_system.models.dto.request.FinReportDTO;
import cloudledger.requisition_system.models.dto.request.IncomeDTO;
import cloudledger.requisition_system.models.dto.response.GetFinReportsDTO;
import cloudledger.requisition_system.models.dto.response.GetRequisitionDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.models.mappers.IncomeMappers;
import cloudledger.requisition_system.repository.ExpenditureRepository;
import cloudledger.requisition_system.repository.FinReportRepository;
import cloudledger.requisition_system.repository.IncomeRepository;
import cloudledger.requisition_system.repository.ProjectRepository;
import cloudledger.requisition_system.service.FinReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinReportServiceImpl implements FinReportService {
    private final FinReportRepository finReportRepository;
    private final ProjectRepository projectRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenditureRepository expenditureRepository;

    @Override
    public TheResponse saveFinanceRecord(Long id, FinReportDTO finReportDTO) {
        // Get the project by ID
        Optional<Project> sources = projectRepository.findProjectById(id);
        Project project = sources.orElseThrow(() ->
                new RunTimeExceptionPlaceHolder("Project doesn't exist!!")
        );

        // Check if a report already exists for the project
        Optional<FinReport> existingFinReport = finReportRepository.findByProject(project);

        if (existingFinReport.isPresent()) {
            // If a report already exists, update it with new income and expenditure entries
            FinReport finReport = existingFinReport.get();

            // Update incomes and expenditures in the existing report
            List<Income> updatedIncomes = finReportDTO.getIncomes().stream()
                    .map(incomeDTO -> Income.builder()
                            .date(incomeDTO.getDate())
                            .amount(incomeDTO.getAmount())
                            .bankReference(incomeDTO.getBankReference())
                            .briefReason(incomeDTO.getBriefReason())
                            .finReport(finReport)
                            .build())
                    .collect(Collectors.toList());

            List<Expenditure> updatedExpenditures = finReportDTO.getExpenditures().stream()
                    .map(expenditureDTO -> Expenditure.builder()
                            .date(expenditureDTO.getDate())
                            .amount(expenditureDTO.getAmount())
                            .bankReference(expenditureDTO.getBankReference())
                            .briefReason(expenditureDTO.getBriefReason())
                            .finReport(finReport)
                            .build())
                    .collect(Collectors.toList());

            // Add updated incomes and expenditures to the existing report
            finReport.getIncomes().addAll(updatedIncomes);
            finReport.getExpenditures().addAll(updatedExpenditures);

            // Save the updated report
            finReportRepository.save(finReport);

            return TheResponse.builder()
                    .message("Financial report for " + project.getName() + " has been successfully updated")
                    .build();
        } else {
            // If no report exists, create a new one
            FinReport finReport = new FinReport();
            finReport.setProject(project);

            // Create new incomes and expenditures and associate them with the new report
            List<Income> incomes = finReportDTO.getIncomes().stream()
                    .map(incomeDTO -> Income.builder()
                            .date(incomeDTO.getDate())
                            .amount(incomeDTO.getAmount())
                            .bankReference(incomeDTO.getBankReference())
                            .briefReason(incomeDTO.getBriefReason())
                            .finReport(finReport)
                            .build())
                    .collect(Collectors.toList());

            List<Expenditure> expenditures = finReportDTO.getExpenditures().stream()
                    .map(expenditureDTO -> Expenditure.builder()
                            .date(expenditureDTO.getDate())
                            .amount(expenditureDTO.getAmount())
                            .bankReference(expenditureDTO.getBankReference())
                            .briefReason(expenditureDTO.getBriefReason())
                            .finReport(finReport)
                            .build())
                    .collect(Collectors.toList());

            // Set incomes and expenditures
            finReport.setIncomes(incomes);
            finReport.setExpenditures(expenditures);

            // Save the new report
            finReportRepository.save(finReport);

            return TheResponse.builder()
                    .message("Financial report for " + project.getName() + " has been successfully created")
                    .build();
        }
    }


    @Override
    public List<GetFinReportsDTO> getFinanceRecordsByProject(Long projectId) {
        Iterable<FinReport> all = finReportRepository.findByProjectId(projectId);
        List<GetFinReportsDTO> allReps = new ArrayList<>();
        all.iterator().forEachRemaining(u->{
            GetFinReportsDTO requisitionResponse = GetFinReportsDTO.builder()
                    .id(u.getId())
                    .project(u.getProject().getName())
                    .incomes(mapIncomesToDTO(u.getIncomes()))
                    .expenditures(mapExpendituresToDTO(u.getExpenditures()))
                    .build();
            allReps.add(requisitionResponse);
        });
        return allReps;
    }

    @Override
    public TheResponse updateIncome(Long id, IncomeDTO updatedIncome) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        income.setDate(updatedIncome.getDate());
        income.setAmount(updatedIncome.getAmount());
        income.setBankReference(updatedIncome.getBankReference());
        income.setBriefReason(updatedIncome.getBriefReason());

        incomeRepository.save(income);
        return TheResponse.builder()
                .message("Income updated successfully")
                .build();
    }

    @Override
    public TheResponse updateExpenditure(Long id, ExpenditureDTO updatedExpenditure) {
        Expenditure expenditure = expenditureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expenditure not found"));

        expenditure.setDate(updatedExpenditure.getDate());
        expenditure.setAmount(updatedExpenditure.getAmount());
        expenditure.setBankReference(updatedExpenditure.getBankReference());
        expenditure.setBriefReason(updatedExpenditure.getBriefReason());

        expenditureRepository.save(expenditure);
        return TheResponse.builder()
                .message("Income updated successfully")
                .build();
    }

    @Override
    public List<GetFinReportsDTO> getAllFinanceRecords() {
        Iterable<FinReport> all = finReportRepository.findAll();
        List<GetFinReportsDTO> allReps = new ArrayList<>();
        all.iterator().forEachRemaining(u->{
            GetFinReportsDTO requisitionResponse = GetFinReportsDTO.builder()
                    .id(u.getId())
                    .project(u.getProject().getName())
                    .incomes(mapIncomesToDTO(u.getIncomes()))
                    .expenditures(mapExpendituresToDTO(u.getExpenditures()))
                    .build();
            allReps.add(requisitionResponse);
        });
        return allReps;
    }
    private List<IncomeDTO> mapIncomesToDTO(List<Income> incomes) {
        if (incomes == null) return new ArrayList<>();

        return incomes.stream().map(income -> IncomeDTO.builder()
                .id(income.getId())
                .date(income.getDate())
                .amount(income.getAmount())
                .bankReference(income.getBankReference())
                .briefReason(income.getBriefReason())
                .build()
        ).collect(Collectors.toList());
    }
    private List<ExpenditureDTO> mapExpendituresToDTO(List<Expenditure> expenditures) {
        if (expenditures == null) return new ArrayList<>();

        return expenditures.stream().map(exp -> ExpenditureDTO.builder()
                .id(exp.getId())
                .date(exp.getDate())
                .amount(exp.getAmount())
                .bankReference(exp.getBankReference())
                .briefReason(exp.getBriefReason())
                .build()
        ).collect(Collectors.toList());
    }
}




