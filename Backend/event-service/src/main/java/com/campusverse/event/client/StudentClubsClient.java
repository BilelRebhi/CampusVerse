package com.campusverse.event.client;

import com.campusverse.event.client.dto.ClubMembershipDTO;
import com.campusverse.event.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Calls MS3 Student Clubs (synchronous REST) to check who manages a club. */
@Slf4j
@Component
public class StudentClubsClient {

    private final RestClient restClient;
    private final boolean enabled;

    public StudentClubsClient(@Qualifier("studentClubsRestClient") RestClient restClient,
                              @Value("${campusverse.integration.enabled:false}") boolean enabled) {
        this.restClient = restClient;
        this.enabled = enabled;
    }

    /**
     * @return true if the user is a manager of the club (always true when integration is disabled)
     * @throws ExternalServiceException if Student Clubs is unreachable
     */
    public boolean isClubManager(Long clubId, Long userId) {
        if (!enabled) {
            log.debug("Integration disabled: assuming user {} manages club {}", userId, clubId);
            return true;
        }
        try {
            ClubMembershipDTO membership = restClient.get()
                    .uri("/api/clubs/{clubId}/members/{userId}", clubId, userId)
                    .retrieve()
                    .body(ClubMembershipDTO.class);
            return membership != null && membership.isManager();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (RestClientException e) {
            throw new ExternalServiceException("Student Clubs service is unavailable: club events cannot be created right now", e);
        }
    }
}
