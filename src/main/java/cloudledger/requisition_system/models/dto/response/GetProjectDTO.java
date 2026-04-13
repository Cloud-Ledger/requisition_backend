package cloudledger.requisition_system.models.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProjectDTO {
    private String id;
    private String name;
    private String description;
}
