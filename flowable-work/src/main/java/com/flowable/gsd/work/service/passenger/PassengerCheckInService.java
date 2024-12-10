package com.flowable.gsd.work.service.passenger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.flowable.content.api.ContentService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.flowable.core.content.api.CoreContentItem;

@Service("passengerCheckInService")
public class PassengerCheckInService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerCheckInService.class);
    private static final String CHECK_IN_PROCESS_DEFINITION_KEY = "passenger_checkin";

    private final ContentService contentService;
    private final RuntimeService runtimeService;
    private final ObjectMapper objectMapper;
    private final RestResponseFactory restResponseFactory;

    public PassengerCheckInService(ContentService contentService, RuntimeService runtimeService, ObjectMapper objectMapper,
            RestResponseFactory restResponseFactory) {
        this.contentService = contentService;
        this.runtimeService = runtimeService;
        this.objectMapper = objectMapper;
        this.restResponseFactory = restResponseFactory;
    }

    @SuppressWarnings("unused")
    public ArrayNode loadPassengerCheckIns(CoreContentItem contentItem) {
        List<ProcessInstance> processInstances = new ArrayList<>();

        try (InputStream is = contentService.getContentItemData(contentItem.getId());
             InputStreamReader reader = new InputStreamReader(is)) {

            CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader("FirstName", "LastName", "Email", "TicketId").build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);

            for (CSVRecord record : records) {
                CheckInDetails checkInDetails = new CheckInDetails(
                        record.get("FirstName"),
                        record.get("LastName"),
                        record.get("Email"),
                        record.get("TicketId")
                );

                ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                        .processDefinitionKey(CHECK_IN_PROCESS_DEFINITION_KEY)
                        .variable("checkInDetails", objectMapper.valueToTree(checkInDetails))
                        .name("Check-in for " + checkInDetails.firstName() + " " + checkInDetails.lastName())
                        .start();

                processInstances.add(processInstance);
            }
        } catch (IOException e) {
            LOGGER.error("Error loading passenger check-ins", e);
        }

        // We cannot directly return the process instances, as they are not serializable
        List<ProcessInstanceResponse> processInstanceResponseList = restResponseFactory.createProcessInstanceResponseList(processInstances);
        return objectMapper.valueToTree(processInstanceResponseList);
    }

    record CheckInDetails(String firstName,
                          String lastName,
                          String email,
                          String ticketId) {
    }
}