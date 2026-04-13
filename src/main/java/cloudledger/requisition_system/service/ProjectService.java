package cloudledger.requisition_system.service;

import cloudledger.requisition_system.models.dao.Project;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.request.ProjectDTO;
import cloudledger.requisition_system.models.dto.response.GetProjectDTO;

import java.util.List;

public interface ProjectService {

    Project createProject(ProjectDTO projectDTO);
    List<GetProjectDTO> getAllProjects();
    List<GetProjectDTO> getUserProjects(Integer id);
    List<GetProjectDTO> getProjectsByUser(User user);
}
