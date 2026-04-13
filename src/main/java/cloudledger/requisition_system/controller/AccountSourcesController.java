package cloudledger.requisition_system.controller;

import cloudledger.requisition_system.models.dto.request.AccountSourceRequest;
import cloudledger.requisition_system.models.dto.request.RequisitionDTO;
import cloudledger.requisition_system.models.dto.response.*;
import cloudledger.requisition_system.service.AccountSourceService;
import cloudledger.requisition_system.utils.Response;
import cloudledger.requisition_system.utils.ResponseBuild;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/sources")
@SecurityRequirement(name = "SECURED GATEWAY API")
@RequiredArgsConstructor
public class AccountSourcesController {
    private final AccountSourceService accountSourceService;
    private final ResponseBuild<TheResponse> theResponseResponseBuild;
    private final ResponseBuild<List<SourcesDTO>> listSourcesResponseBuild;
    // Create a new requisition
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<TheResponse> createSourceAccount(@RequestBody AccountSourceRequest accountSourceRequest) {
        TheResponse accountSource = accountSourceService.createSource(accountSourceRequest);
        return new ResponseEntity<>(accountSource, HttpStatus.CREATED);


    }

    @GetMapping
    public ResponseEntity<Response> getAllSources() {
//        List<SourcesDTO> sources = accountSourceService.getAllSources();
//        return new ResponseEntity<>(sources, HttpStatus.OK);
        return  new ResponseEntity<>(listSourcesResponseBuild.successResponse
                .apply(accountSourceService.getAllSources(),null), HttpStatus.CREATED);
    }

}
