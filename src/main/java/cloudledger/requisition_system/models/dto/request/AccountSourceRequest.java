package cloudledger.requisition_system.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountSourceRequest {

    private String name;
    private String accountNumber;
    private String currency;
    private Integer balance;
}
