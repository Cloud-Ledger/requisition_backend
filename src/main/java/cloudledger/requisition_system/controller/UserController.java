package cloudledger.requisition_system.controller;

import cloudledger.requisition_system.models.dto.request.UpdateUserDTO;
import cloudledger.requisition_system.models.dto.response.GetUserDTO;
import cloudledger.requisition_system.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/u")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<GetUserDTO>> getAllUsers() {
        List<GetUserDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<GetUserDTO> getUser(@PathVariable("email") Optional<String> email) {

        GetUserDTO user = null;
        if (email.isPresent()) {
            user = userService.getUserByUserName(email.get());
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {

        return userService.deleteUser(id);
    }
    @PostMapping("/{userId}/assign/{projectId}")
    public ResponseEntity<String> assignUserToProject(
            @PathVariable Integer userId,
            @PathVariable Long projectId) {
        userService.assignUserToProject(userId, projectId);
        return ResponseEntity.ok("User assigned to project successfully.");
    }

    @PutMapping("/update/{id}")
    public String createTracking(@PathVariable("id") Integer id, @RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO, id);
    }
}
