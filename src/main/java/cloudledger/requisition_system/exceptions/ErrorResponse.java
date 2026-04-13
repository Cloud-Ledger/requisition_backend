package cloudledger.requisition_system.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

  private String errorMsg;

  private String developerMsg;

  private HttpStatus responseStatus;

  private int responseCode;


}
