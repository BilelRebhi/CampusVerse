package com.campusverse.event.client.dto;

/** Room data received from MS1 Campus Spaces: GET /api/spaces/{id} */
public record SpaceDTO(
        Long id,
        String name,
        Integer capacity,
        Boolean available
) {
}
