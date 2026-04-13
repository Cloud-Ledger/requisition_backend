package cloudledger.requisition_system.service;


import cloudledger.requisition_system.config.JwtService;
import cloudledger.requisition_system.exceptions.RunTimeExceptionPlaceHolder;
import cloudledger.requisition_system.models.dao.Project;
import cloudledger.requisition_system.models.dao.Role;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.response.LoginResponse;
import cloudledger.requisition_system.repository.ProjectRepository;
import cloudledger.requisition_system.repository.UserRepository;
import cloudledger.requisition_system.models.dto.request.RegisterRequest;
import cloudledger.requisition_system.models.dto.request.AuthenticationRequest;
import cloudledger.requisition_system.models.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final ProjectRepository projectRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RunTimeExceptionPlaceHolder("Username is already taken!!");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .phoneNumber(request.getPhoneNumber())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode("Passzim12#"))
                .status("Inactive")
                .role(Role.valueOf(request.getRole().name()))
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .message("Registration Successful")
                .email(savedUser.getEmail())
                .build();
    }

  public LoginResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(()-> new RunTimeExceptionPlaceHolder("Bad credentials"));
    var jwtToken = jwtService.generateToken(user.getEmail());

    user.isAccountNonLocked();
    return LoginResponse.builder()
            .accessToken(jwtToken)
            .firstName(user.getFirstname())
            .lastName(user.getLastname())
            .data(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .role(user.getRole().name())
            .status(user.getStatus())
            .build();

  }


}
