package cloudledger.requisition_system.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @JsonProperty("token")
    private String accessToken;
    @JsonProperty("data")
    private String data;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("role")
    private String role;
    @JsonProperty("status")
    private String status;

}
