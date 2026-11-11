package com.campusverse.event.client.dto;

/** Membership returned by MS3 Student Clubs: GET /api/clubs/{clubId}/members/{userId} */
public record ClubMembershipDTO(
        Long clubId,
        Long userId,
        String role
) {
    public boolean isManager() {
        return role != null && (role.equalsIgnoreCase("MANAGER") || role.equalsIgnoreCase("PRESIDENT"));
    }
}
