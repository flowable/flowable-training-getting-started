package com.flowable.gsd.work.rest.booking;

public class BookFlightResponse {

    private String processInstanceId;

    public BookFlightResponse(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
