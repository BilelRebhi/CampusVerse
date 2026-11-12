package com.campusverse.municipal.repository;

import com.campusverse.municipal.model.MunicipalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MunicipalServiceRepository extends JpaRepository<MunicipalService, UUID> {
}
