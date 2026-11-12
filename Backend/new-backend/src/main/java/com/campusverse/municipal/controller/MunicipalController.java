package com.campusverse.municipal.controller;

import com.campusverse.municipal.dto.CreateApplicationDto;
import com.campusverse.municipal.dto.UpdateStatusDto;
import com.campusverse.municipal.model.ApplicationRequest;
import com.campusverse.municipal.model.MunicipalService;
import com.campusverse.municipal.model.RequestStatus;
import com.campusverse.municipal.repository.ApplicationRequestRepository;
import com.campusverse.municipal.repository.MunicipalServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/municipal")
@CrossOrigin(origins = "*")
public class MunicipalController {

    private final MunicipalServiceRepository serviceRepository;
    private final ApplicationRequestRepository applicationRepository;

    public MunicipalController(MunicipalServiceRepository serviceRepository,
                               ApplicationRequestRepository applicationRepository) {
        this.serviceRepository = serviceRepository;
        this.applicationRepository = applicationRepository;
    }

    // ---------------------- Services Catalogue ----------------------

    @GetMapping("/services")
    public List<MunicipalService> getAllServices() {
        return serviceRepository.findAll();
    }

    @GetMapping("/services/{id}")
    public MunicipalService getServiceById(@PathVariable UUID id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipal service not found"));
    }

    @PostMapping("/services")
    public ResponseEntity<MunicipalService> createService(@RequestBody MunicipalService service) {
        MunicipalService created = serviceRepository.save(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------------- Application Requests ----------------------

    @PostMapping("/applications")
    public ResponseEntity<ApplicationRequest> submitApplication(@RequestBody CreateApplicationDto dto) {
        if (dto.getServiceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ID is required");
        }

        MunicipalService service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipal service not found"));

        ApplicationRequest request = new ApplicationRequest();
        request.setCitizenId(dto.getCitizenId() != null ? dto.getCitizenId() : UUID.randomUUID());
        request.setService(service);
        request.setStatus(RequestStatus.PENDING);

        ApplicationRequest saved = applicationRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/applications/{id}")
    public ApplicationRequest getApplicationById(@PathVariable UUID id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));
    }

    @GetMapping("/applications/citizen/{citizenId}")
    public List<ApplicationRequest> getApplicationsByCitizen(@PathVariable UUID citizenId) {
        return applicationRepository.findByCitizenId(citizenId);
    }

    @PatchMapping("/applications/{id}/status")
    public ApplicationRequest updateApplicationStatus(@PathVariable UUID id, @RequestBody UpdateStatusDto dto) {
        ApplicationRequest request = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));

        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
        }
        return applicationRepository.save(request);
    }
}
