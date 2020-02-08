package com.automation.api.Tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static com.automation.api.Data.STATUSCODE_CREATED;
import static com.automation.api.Data.STATUSCODE_OK;
import static com.automation.api.Tests.CommonAPI.createEvent;
import static com.automation.api.utils.CommonMethods.getValueFromJsonPath;
import static com.automation.api.utils.CommonMethods.loadPropertiesAndLoginUser;
import static com.automation.api.utils.PropertyUtils.getPropertyByKey;
import static com.automation.api.utils.RequestUtils.jerseyRequestForAPIInfo;
import static com.automation.api.utils.VerificationUtils.verifyResponseFromJsonPath;
import static com.automation.api.utils.VerificationUtils.verifyResponseStatusCode;

public class CopyRightAPI {
    @BeforeClass
    public void beforeClass() {
        loadPropertiesAndLoginUser();
        createEvent();
    }

    @Test(priority = 0, description = "Create a new Event Copyright.")
    public void postCreateEventCopyRight() {
        String holdername = RandomStringUtils.randomAlphabetic(5);
        String licenceName = RandomStringUtils.randomAlphanumeric(5);
        String eventId = System.getProperty("event-id");

        String apiData = String.format(getPropertyByKey("post.create.event.copyright"), eventId, holdername, licenceName);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_CREATED, response);

        String responseData = response.readEntity(String.class);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.holder", holdername);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.licence", licenceName);

        String idOfEventCopyRight = getValueFromJsonPath(responseData, "$.data.id");
        System.setProperty("EventCopyRightID", idOfEventCopyRight);
        System.setProperty("holdername", holdername);
    }

    @Test(priority = 1, description = "EVENT COPYRIGHT DETAILS")
    public void getEventCopyRightDetails() {
        String EventCopyRightID = System.getProperty("EventCopyRightID");
        String apiData = String.format(getPropertyByKey("get.event.copyright.details"), EventCopyRightID);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.data.attributes.holder", System.getProperty("holdername"));
    }

    @Test(priority = 2, description = "Update copyright details")
    public void updateCopyRightDetails() {
        String eventCopyRightID = System.getProperty("EventCopyRightID");
        String holderUpdatedName = RandomStringUtils.randomAlphanumeric(7);
        String apiData = String.format(getPropertyByKey("patch.event.copyright.by.id"), eventCopyRightID, holderUpdatedName, eventCopyRightID);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.data.attributes.holder", holderUpdatedName);
    }

    @Test(priority = 3, description = "Delete a single event copyright.")
    public void deleteEventCopyRight() {
        String idOfEventCopyRight = System.getProperty("EventCopyRightID");
        String apiData = String.format(getPropertyByKey("delete.event.copyright"), idOfEventCopyRight);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.meta.message", "Object successfully deleted");
    }


}
