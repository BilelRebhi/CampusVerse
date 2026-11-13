package com.campusverse.municipal.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class MunicipalService {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer processingDelayDays;

    // getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getProcessingDelayDays() { return processingDelayDays; }
    public void setProcessingDelayDays(Integer processingDelayDays) { this.processingDelayDays = processingDelayDays; }
}
