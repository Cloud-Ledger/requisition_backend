package cloudledger.requisition_system.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourcesDTO {
    private Long id;
    private String name;
    private String accountNumber;
    private String currency;
    private double balance;
}
