package cloudledger.requisition_system.repository;

import cloudledger.requisition_system.models.dao.Comment;
import cloudledger.requisition_system.models.dao.FinReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFinReportAndParentCommentIsNull(FinReport finReport);

}
