package cloudledger.requisition_system.service.service_impl;

import cloudledger.requisition_system.models.dao.Project;
import cloudledger.requisition_system.models.dao.Role;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.request.ProjectDTO;
import cloudledger.requisition_system.models.dto.response.GetProjectDTO;
import cloudledger.requisition_system.models.dto.response.GetUserDTO;
import cloudledger.requisition_system.repository.ProjectRepository;
import cloudledger.requisition_system.repository.UserRepository;
import cloudledger.requisition_system.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    @Override
    public Project createProject(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .build();
        return projectRepository.save(project);
    }

    @Override
    public List<GetProjectDTO> getAllProjects() {
        Iterable<Project> all = projectRepository.findAll();
        List<GetProjectDTO> allReqs = new ArrayList<>();
        all.iterator().forEachRemaining(u->{
            GetProjectDTO requisitionResponse = GetProjectDTO.builder()
                    .id(String.valueOf(u.getId()))
                    .name(u.getName())
                    .description(u.getDescription())
                    .build();
            allReqs.add(requisitionResponse);
        });
        return allReqs;
    }
    @Override
    public List<GetProjectDTO> getUserProjects(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // If user is ADMIN, return all projects
        if (user.getRole() == Role.ADMIN) {
            Iterable<Project> all = projectRepository.findAll();
            List<GetProjectDTO> allProjects = new ArrayList<>();
            all.iterator().forEachRemaining(u->{
                GetProjectDTO userResponse = GetProjectDTO.builder()
                        .id(String.valueOf(u.getId()))
                        .name(u.getName())
                        .description(u.getDescription())
                        .build();
                allProjects.add(userResponse);
            });
            return allProjects;
        } else {
            Iterable<Project> assigned = projectRepository.findProjectsByUserId(userId);
            List<GetProjectDTO> assignedProjects = new ArrayList<>();
            assigned.iterator().forEachRemaining(u -> {
                GetProjectDTO projects = GetProjectDTO.builder()
                        .id(String.valueOf(u.getId()))
                        .name(u.getName())
                        .description(u.getDescription())
                        .build();
                assignedProjects.add(projects);
            });
            return assignedProjects;
        }
    }
    @Override
    public List<GetProjectDTO> getProjectsByUser(User user) {
        if (user.getRole() == Role.ADMIN) {
            Iterable<Project> all = projectRepository.findAll();
            List<GetProjectDTO> allProjects = new ArrayList<>();
            all.iterator().forEachRemaining(u->{
                GetProjectDTO userResponse = GetProjectDTO.builder()
                        .id(String.valueOf(u.getId()))
                        .name(u.getName())
                        .description(u.getDescription())
                        .build();
                allProjects.add(userResponse);
            });
            return allProjects; // Admin can see all projects
        } else {
            Iterable<Project> assigned = projectRepository.findProjectsByUserId(user.getId());
            List<GetProjectDTO> assignedProjects = new ArrayList<>();
            assigned.iterator().forEachRemaining(u->{
                GetProjectDTO projects = GetProjectDTO.builder()
                        .id(String.valueOf(u.getId()))
                        .name(u.getName())
                        .description(u.getDescription())
                        .build();
                assignedProjects.add(projects);
            });
            return assignedProjects; // Normal users see only assigned projects
        }
    }

}
