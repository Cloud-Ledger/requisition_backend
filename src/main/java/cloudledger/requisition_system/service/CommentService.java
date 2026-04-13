package cloudledger.requisition_system.service;

import cloudledger.requisition_system.models.dao.Comment;
import cloudledger.requisition_system.models.dto.response.CommentsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;

import java.util.List;

public interface CommentService {
    TheResponse saveComment(Long userId, Long finReportId, String commentText, Long parentCommentId);
    List<CommentsDTO> getCommentsForFinReport(Long finReportId);

}
