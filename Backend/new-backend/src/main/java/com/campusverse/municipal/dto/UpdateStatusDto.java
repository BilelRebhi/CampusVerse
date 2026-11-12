package com.campusverse.municipal.dto;

import com.campusverse.municipal.model.RequestStatus;

public class UpdateStatusDto {
    private RequestStatus status;

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
