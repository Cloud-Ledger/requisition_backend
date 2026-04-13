package cloudledger.requisition_system.controller;

import cloudledger.requisition_system.config.JwtService;
import cloudledger.requisition_system.repository.UserRepository;
import cloudledger.requisition_system.utils.Response;
import cloudledger.requisition_system.utils.ResponseBuild;
import cloudledger.requisition_system.models.dto.request.AuthenticationRequest;
import cloudledger.requisition_system.models.dto.response.AuthenticationResponse;
import cloudledger.requisition_system.models.dto.response.LoginResponse;
import cloudledger.requisition_system.models.dto.request.RegisterRequest;
import cloudledger.requisition_system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final JwtService jwtService;


  private final ResponseBuild<LoginResponse> loginResponseResponseBuild;
  private final ResponseBuild<AuthenticationResponse> authenticationResponseResponseBuild;

  private final UserRepository userRepository;

  @PostMapping("/register")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Response> register(
      @RequestBody RegisterRequest request
  ) {
    return  new ResponseEntity<>(authenticationResponseResponseBuild.successResponse
            .apply(authenticationService.register(request),null), HttpStatus.CREATED);
  }
  @PostMapping("/authenticate")
  public ResponseEntity<Response> authenticate(
      @RequestBody AuthenticationRequest request
  ) {

    return  new ResponseEntity<>(loginResponseResponseBuild.successResponse
            .apply(authenticationService.authenticate(request),null), HttpStatus.CREATED);
  }



}
