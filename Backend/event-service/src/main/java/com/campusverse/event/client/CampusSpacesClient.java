package com.campusverse.event.client;

import com.campusverse.event.client.dto.SpaceDTO;
import com.campusverse.event.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

/** Calls MS1 Campus Spaces (synchronous REST). */
@Slf4j
@Component
public class CampusSpacesClient {

    private final RestClient restClient;
    private final boolean enabled;

    public CampusSpacesClient(@Qualifier("campusSpacesRestClient") RestClient restClient,
                              @Value("${campusverse.integration.enabled:false}") boolean enabled) {
        this.restClient = restClient;
        this.enabled = enabled;
    }

    /**
     * @return the space, or empty if integration is disabled or the space does not exist
     * @throws ExternalServiceException if Campus Spaces is unreachable
     */
    public Optional<SpaceDTO> getSpace(Long spaceId) {
        if (!enabled) {
            log.debug("Integration disabled: skipping Campus Spaces check for space {}", spaceId);
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/api/spaces/{id}", spaceId)
                    .retrieve()
                    .body(SpaceDTO.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Campus Spaces service is unavailable", e);
        }
    }
}
