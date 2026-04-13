package cloudledger.requisition_system.service;

import cloudledger.requisition_system.models.dao.FinReport;
import cloudledger.requisition_system.models.dto.request.ExpenditureDTO;
import cloudledger.requisition_system.models.dto.request.FinReportDTO;
import cloudledger.requisition_system.models.dto.request.IncomeDTO;
import cloudledger.requisition_system.models.dto.response.GetFinReportsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;

import java.util.List;

public interface FinReportService {
    List<GetFinReportsDTO> getAllFinanceRecords();
    TheResponse saveFinanceRecord(Long id, FinReportDTO finReportDTO);
    List<GetFinReportsDTO> getFinanceRecordsByProject(Long projectId);
    TheResponse updateIncome(Long id, IncomeDTO updatedIncome);
    TheResponse updateExpenditure(Long id, ExpenditureDTO updatedExpenditure);
}
