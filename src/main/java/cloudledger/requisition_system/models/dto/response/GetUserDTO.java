package cloudledger.requisition_system.models.dto.response;

import cloudledger.requisition_system.models.dao.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserDTO {

  private Integer id;
  private String userName;
  private String fullName;
  private  String  phoneNumber;
  private String email;
  private Role role;
  private String status;
  private Boolean locked;

}
