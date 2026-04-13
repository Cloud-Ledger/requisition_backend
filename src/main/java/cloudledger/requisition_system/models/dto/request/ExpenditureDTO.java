package cloudledger.requisition_system.models.dto.request;
import cloudledger.requisition_system.models.dao.Attachments;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenditureDTO {
    private Long id;
    private LocalDate date;
    private Integer amount;
    private String bankReference;
    private String briefReason;
    //private List<Attachments> attachmentsList;
}