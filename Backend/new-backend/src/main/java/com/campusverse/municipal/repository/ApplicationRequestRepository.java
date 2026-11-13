package com.campusverse.municipal.repository;

import com.campusverse.municipal.model.ApplicationRequest;
import com.campusverse.municipal.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRequestRepository extends JpaRepository<ApplicationRequest, UUID> {
    List<ApplicationRequest> findByCitizenId(UUID citizenId);
    List<ApplicationRequest> findByStatus(RequestStatus status);
}
