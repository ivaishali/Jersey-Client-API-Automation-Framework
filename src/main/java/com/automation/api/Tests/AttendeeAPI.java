package com.automation.api.Tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static com.automation.api.Data.STATUSCODE_CREATED;
import static com.automation.api.Data.STATUSCODE_OK;
import static com.automation.api.utils.CommonMethods.getValueFromJsonPath;
import static com.automation.api.utils.CommonMethods.loadPropertiesAndLoginUser;
import static com.automation.api.utils.PropertyUtils.getPropertyByKey;
import static com.automation.api.utils.RequestUtils.jerseyRequestForAPIInfo;
import static com.automation.api.utils.VerificationUtils.verifyResponseFromJsonPath;
import static com.automation.api.utils.VerificationUtils.verifyResponseStatusCode;

public class AttendeeAPI {
    @BeforeClass
    public void beforeClass() {
        loadPropertiesAndLoginUser();
    }

    @Test(priority = 0, description = "Create a new Attendee")
    public void postCreateEventCopyRight() {
        String firstName = RandomStringUtils.randomAlphanumeric(7);
        String lastName = RandomStringUtils.randomAlphanumeric(8);

        String apiData = String.format(getPropertyByKey("post.create.attendee"), firstName, lastName);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_CREATED, response);

        String responseData = response.readEntity(String.class);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.lastname", lastName);

        System.setProperty("attendeeLastName", lastName);
        String AttendeeID = getValueFromJsonPath(responseData, "$.data.id");
        System.setProperty("AttendeeID", AttendeeID);
    }

    @Test(priority = 1, description = "GET ATTENDEE DETAILS")
    public void getEventCopyRightDetails() {
        String AttendeeID = System.getProperty("AttendeeID");
        String apiData = String.format(getPropertyByKey("get.attendee.detail"), AttendeeID);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.data.attributes.lastname", System.getProperty("attendeeLastName"));
    }

    @Test(priority = 2, description = "Update attendee details")
    public void updateCopyRightDetails() {
        String AttendeeID = System.getProperty("AttendeeID");
        String updatedLastName = RandomStringUtils.randomAlphanumeric(7);
        String apiData = String.format(getPropertyByKey("patch.update.attendee.detail"), AttendeeID, updatedLastName, AttendeeID);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.data.attributes.lastname", updatedLastName);
    }

    @Test(priority = 3, description = "Delete a Attendee")
    public void deleteEventCopyRight() {
        String AttendeeID = System.getProperty("AttendeeID");
        String apiData = String.format(getPropertyByKey("delete.attendee.profile"), AttendeeID);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.meta.message", "Object successfully deleted");
    }


}


