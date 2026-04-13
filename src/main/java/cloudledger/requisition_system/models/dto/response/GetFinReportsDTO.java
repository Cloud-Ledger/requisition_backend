package cloudledger.requisition_system.models.dto.response;

import cloudledger.requisition_system.models.dto.request.ExpenditureDTO;
import cloudledger.requisition_system.models.dto.request.IncomeDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFinReportsDTO {
    private Long id;
    private String project;
    private List<IncomeDTO> incomes;
    private List<ExpenditureDTO> expenditures;
}
