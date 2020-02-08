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

public class SpeakerAPI {
    @BeforeClass
    public void beforeClass() {
        loadPropertiesAndLoginUser();
        createEvent();
    }

    @Test(priority = 0, description = "Create a Speaker profile")
    public void postCreateSpeakerProfile() {
        String eventId = System.getProperty("event-id");
        String sessionName = RandomStringUtils.randomAlphanumeric(10);

        String apiData = String.format(getPropertyByKey("post.create.speaker.profile"), eventId, sessionName);
        Response response = jerseyRequestForAPIInfo(apiData);
        String responseData = response.readEntity(String.class);
        verifyResponseStatusCode(STATUSCODE_CREATED, response);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.name", sessionName);
        String idOfSpeakerProfile = getValueFromJsonPath(responseData, "$.data.id");
        System.setProperty("idOfSpeakerProfile", idOfSpeakerProfile);
        System.setProperty("sessionName", sessionName);
    }

    @Test(priority = 1, description = "Get a single Speaker profile by ID")
    public void getSpeakerProfileDetailsByID() {
        String idOfSpeakerProfile = System.getProperty("idOfSpeakerProfile");
        String sessionName = System.getProperty("sessionName");
        String apiData = String.format(getPropertyByKey("get.speaker.profile.detail"), idOfSpeakerProfile, sessionName, sessionName);
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.data.attributes.name", sessionName);
    }

    @Test(priority = 2, description = "Update Speaker profile by ID")
    public void updateSpeakerProfileByID() {
        String idOfSpeakerProfile = System.getProperty("idOfSpeakerProfile");
        String sessionUpdatedName = RandomStringUtils.randomAlphanumeric(10);

        String apiData = String.format(getPropertyByKey("patch.update.speaker.profile.detail"), idOfSpeakerProfile, sessionUpdatedName, idOfSpeakerProfile);
        Response response = jerseyRequestForAPIInfo(apiData);
        String responseData = response.readEntity(String.class);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.name", sessionUpdatedName);
    }

    @Test(priority = 3, description = "Delete a single speaker by id")
    public void deleteSpeakerProfileDetailsByID() {
        String idOfSpeakerProfile = System.getProperty("idOfSpeakerProfile");
        String apiData = String.format(getPropertyByKey("delete.sepaker.profile"), idOfSpeakerProfile);
        Response response = jerseyRequestForAPIInfo(apiData);
        String responseData = response.readEntity(String.class);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(responseData, "$.meta.message", "Object successfully deleted");
    }
}
