package cloudledger.requisition_system.models.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinReportDTO {
    private List<IncomeDTO> incomes;
    private List<ExpenditureDTO> expenditures;
}
