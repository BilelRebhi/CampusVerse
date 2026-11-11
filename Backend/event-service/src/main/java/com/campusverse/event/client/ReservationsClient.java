package com.campusverse.event.client;

import com.campusverse.event.client.dto.ReservationDTO;
import com.campusverse.event.client.dto.ReservationRequestDTO;
import com.campusverse.event.entity.Event;
import com.campusverse.event.exception.BusinessRuleException;
import com.campusverse.event.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Calls MS2 Reservations (synchronous REST) to book / release the event's room. */
@Slf4j
@Component
public class ReservationsClient {

    private final RestClient restClient;
    private final boolean enabled;

    public ReservationsClient(@Qualifier("reservationsRestClient") RestClient restClient,
                              @Value("${campusverse.integration.enabled:false}") boolean enabled) {
        this.restClient = restClient;
        this.enabled = enabled;
    }

    /**
     * Books the room of the event.
     *
     * @return the reservation id, or null if integration is disabled
     * @throws BusinessRuleException    if the room is already taken (409 from Reservations)
     * @throws ExternalServiceException if Reservations is unreachable
     */
    public Long reserveRoom(Event event) {
        if (!enabled) {
            log.debug("Integration disabled: skipping room reservation for event {}", event.getId());
            return null;
        }
        ReservationRequestDTO body = new ReservationRequestDTO(
                event.getSpaceId(), event.getStartDateTime(), event.getEndDateTime(),
                event.getOrganizerId(), "Event: " + event.getTitle(), event.getId());
        try {
            ReservationDTO reservation = restClient.post()
                    .uri("/api/reservations")
                    .body(body)
                    .retrieve()
                    .body(ReservationDTO.class);
            if (reservation == null || reservation.id() == null) {
                throw new ExternalServiceException("Reservations service returned an empty response");
            }
            return reservation.id();
        } catch (HttpClientErrorException.Conflict e) {
            throw new BusinessRuleException("The room is not available for this time slot");
        } catch (RestClientException e) {
            throw new ExternalServiceException(
                    "Reservations service is unavailable: the event stays PENDING_ROOM, try publishing again later", e);
        }
    }

    /** Releases a reservation (best effort: a failure is only logged). */
    public void cancelReservation(Long reservationId) {
        if (!enabled || reservationId == null) {
            return;
        }
        try {
            restClient.delete().uri("/api/reservations/{id}", reservationId).retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.warn("Could not release reservation {}: {}", reservationId, e.getMessage());
        }
    }
}
