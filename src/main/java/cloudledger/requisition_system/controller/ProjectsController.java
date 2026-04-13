package cloudledger.requisition_system.controller;

import cloudledger.requisition_system.models.dao.Project;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.request.ProjectDTO;
import cloudledger.requisition_system.models.dto.response.GetProjectDTO;
import cloudledger.requisition_system.models.dto.response.GetUserDTO;
import cloudledger.requisition_system.service.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/projects")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class ProjectsController {

    private final ProjectService projectService;
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<GetProjectDTO>> getMyProjects(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(projectService.getProjectsByUser(user));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<GetProjectDTO>> getUser(@PathVariable("id") Integer id) {

        return ResponseEntity.ok(projectService.getUserProjects(id));

    }
}
