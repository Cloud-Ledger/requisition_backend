package cloudledger.requisition_system.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String firstname;
    private String phoneNumber;
    private String lastname;
    private String email;
    private String password;
    private String status;
}
