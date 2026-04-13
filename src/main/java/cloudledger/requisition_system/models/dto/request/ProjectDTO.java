package cloudledger.requisition_system.models.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private String name;
    private String description;

}
