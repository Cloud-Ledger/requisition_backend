package cloudledger.requisition_system.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO {

    private Long id;
    private String comment;
    private List<CommentsDTO> replies;
    private String report;
    private String user;
    private String username;
    private Integer userId;
}
