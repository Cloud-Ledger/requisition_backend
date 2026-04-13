package cloudledger.requisition_system.service.service_impl;

import cloudledger.requisition_system.models.dao.Comment;
import cloudledger.requisition_system.models.dao.FinReport;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.response.CommentsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.repository.CommentRepository;
import cloudledger.requisition_system.repository.FinReportRepository;
import cloudledger.requisition_system.repository.UserRepository;
import cloudledger.requisition_system.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FinReportRepository finReportRepository;


    @Override
    public TheResponse saveComment(Long userId, Long finReportId, String commentText, Long parentCommentId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        FinReport finReport = finReportRepository.findById(finReportId)
                .orElseThrow(() -> new EntityNotFoundException("FinReport not found with ID: " + finReportId));

        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent Comment not found with ID: " + parentCommentId));
        }

        Comment comment = Comment.builder()
                .user(user)
                .finReport(finReport)
                .commentText(commentText)
                .createdAt(LocalDateTime.now())
                .parentComment(parentComment)
                .build();

        commentRepository.save(comment);
        return  TheResponse.builder()
                .message("Comment saved successfully")
                .build();
    }

    @Override
    public List<CommentsDTO> getCommentsForFinReport(Long finReportId) {
        FinReport finReport = finReportRepository.findById(finReportId)
                .orElseThrow(() -> new EntityNotFoundException("FinReport not found with ID: " + finReportId));

        List<Comment> comments = commentRepository.findByFinReportAndParentCommentIsNull(finReport);

        return comments.stream().map(this::convertToDTO).toList();
    }

    private CommentsDTO convertToDTO(Comment comment) {
        return CommentsDTO.builder()
                .id(comment.getId())
                .comment(comment.getCommentText())
                .report(comment.getFinReport().getProject().getName()) // Assuming project has a name
                .user(comment.getUser().getUsername()) // Assuming user has a username
                .replies(comment.getReplies() != null ? comment.getReplies().stream().map(this::convertToDTO).toList() : new ArrayList<>())
                .username(comment.getUser().getFirstname())
                .userId(comment.getUser().getId())
                .build();
    }
}
