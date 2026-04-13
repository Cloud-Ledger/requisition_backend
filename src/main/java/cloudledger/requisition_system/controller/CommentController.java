package cloudledger.requisition_system.controller;

import cloudledger.requisition_system.models.dao.Comment;
import cloudledger.requisition_system.models.dto.response.CommentsDTO;
import cloudledger.requisition_system.models.dto.response.TheResponse;
import cloudledger.requisition_system.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/comment")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class CommentController {
private final CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<TheResponse> createComment(@RequestParam Long userId,
                                                     @RequestParam Long finReportId,
                                                     @RequestParam String comment,
                                                     @RequestParam(required = false) Long parentCommentId) {
         ;
        return ResponseEntity.ok(commentService.saveComment(userId, finReportId, comment, parentCommentId));
    }
    @GetMapping("/view/")
    public ResponseEntity<List<CommentsDTO>> getComments(@RequestParam Long finReportId) {
        return ResponseEntity.ok(commentService.getCommentsForFinReport(finReportId));
    }
}
