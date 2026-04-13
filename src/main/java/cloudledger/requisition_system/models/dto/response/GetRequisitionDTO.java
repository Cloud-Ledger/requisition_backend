package cloudledger.requisition_system.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRequisitionDTO {
    private Long id;
    private String requisitionNumber;
    private LocalDate date;
    private String beneficiary;
    private Integer amount;
    private String bankReference;
    private String briefReason;
    private String status;
    private String userEmail;
    private String source;

}
