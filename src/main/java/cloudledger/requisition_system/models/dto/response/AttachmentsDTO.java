package cloudledger.requisition_system.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentsDTO {
    private String id;
    private String name;
    private String path;
    private String requisitionNumber;
    private String expReference;
    private String incReference;
    private LocalDate reqDate;
    private LocalDate expDate;
    private LocalDate incDate;


}
