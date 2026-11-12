package com.campusverse.municipal.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ApplicationRequest {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID citizenId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private MunicipalService service;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCitizenId() { return citizenId; }
    public void setCitizenId(UUID citizenId) { this.citizenId = citizenId; }
    public MunicipalService getService() { return service; }
    public void setService(MunicipalService service) { this.service = service; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
}
