package cloudledger.requisition_system.models.dto.request;

import cloudledger.requisition_system.models.dao.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String phoneNumber;
  private String lastname;
  private String email;
  private String password;
  private String status;
  private Role role;
  private List<Long> projectIds;

}
