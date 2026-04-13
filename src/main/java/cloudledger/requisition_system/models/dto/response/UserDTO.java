package cloudledger.requisition_system.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String status;
}
