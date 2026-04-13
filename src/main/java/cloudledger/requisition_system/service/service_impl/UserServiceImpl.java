package cloudledger.requisition_system.service.service_impl;


import cloudledger.requisition_system.exceptions.RunTimeExceptionPlaceHolder;
import cloudledger.requisition_system.models.dao.Project;
import cloudledger.requisition_system.models.dao.Role;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.request.UpdateUserDTO;
import cloudledger.requisition_system.models.dto.response.AuthenticationResponse;
import cloudledger.requisition_system.repository.ProjectRepository;
import cloudledger.requisition_system.repository.UserRepository;
import cloudledger.requisition_system.models.dto.response.GetUserDTO;
import cloudledger.requisition_system.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

  private final PasswordEncoder passwordEncoder;


  @Override
  public GetUserDTO getUserByUserName(String userName) {

    Optional<User> userNameOrEmailOptional = userRepository.findByEmail(userName);
    User userByUserName = userNameOrEmailOptional.orElseThrow(() ->
        new RunTimeExceptionPlaceHolder("UserName or Email doesn't exist!!")
    );

    return GetUserDTO.builder()
       .id(userByUserName.getId())
        .userName(userByUserName.getUsername())
            .fullName(userByUserName.getFirstname())
            .locked(userByUserName.isAccountNonLocked())
            .phoneNumber(userByUserName.getPhoneNumber())
        .email(userByUserName.getEmail())
        .role(userByUserName.getRole())
        .status(userByUserName.getStatus())
        .build();
  }

  @Override
  public List<GetUserDTO> getAllUsers() {
    Iterable<User> all = userRepository.findAll();
    List<GetUserDTO> allUsers = new ArrayList<>();
    all.iterator().forEachRemaining(u->{
      GetUserDTO userResponse = GetUserDTO.builder()
               .id(u.getId())
              .userName(u.getUsername())
              .phoneNumber(u.getPhoneNumber())
            //  .logInType(u.getLogInType())
              .fullName(u.getFirstname())
              .email(u.getEmail())
              .role(u.getRole())
              .status(u.getStatus())
             .locked(u.isAccountNonLocked())
              .build();
      allUsers.add(userResponse);
    });

    return allUsers;
  }

  @Override
  public String deleteUser(Integer id) {
    User user = userRepository.findUserById(id);
    userRepository.delete(user);
    return "User deleted successfully";
  }
  @Override
  public User assignUserToProject(Integer userId, Long projectId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    user.getAssignedProjects().add(project);
    return userRepository.save(user);
  }

  @Override
  public String updateUser(UpdateUserDTO request, Integer userId) {
    Optional<User> user = userRepository.findById(userId);

    if (user.isPresent()){
      User u = userRepository.findUserById(userId);

              u.setFirstname(u.getFirstname());
              u.setPhoneNumber(u.getPhoneNumber());
              u.setLastname(u.getLastname());
              u.setEmail(u.getEmail());
              u.setPassword(u.getPassword());
              u.setStatus(request.getStatus());

      userRepository.save(u);
      return "Account updated successfully";
    } else{
      throw new RunTimeExceptionPlaceHolder("Account with this id is not found");
    }

  }

}
