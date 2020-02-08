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

public class MicroLocations {
    @BeforeClass
    public void beforeClassMethod() {
        loadPropertiesAndLoginUser();
    }

    @Test(priority = 0, description = "Create a new microlocation using an event_id.")
    public void postCreateMicroLocations() {
        String microLocationName = RandomStringUtils.randomAlphanumeric(5);
        String latitude = RandomStringUtils.randomNumeric(1);
        String longitude = RandomStringUtils.randomNumeric(1);
        String floor = RandomStringUtils.randomNumeric(2);
        String room = RandomStringUtils.randomNumeric(1);

        String apiData = String.format(getPropertyByKey("post.create.micro.location"), microLocationName, latitude, longitude, floor, room);
        Response response = jerseyRequestForAPIInfo(apiData);
        String responseData = response.readEntity(String.class);
        verifyResponseStatusCode(STATUSCODE_CREATED, response);

        verifyResponseFromJsonPath(responseData, "$.data.attributes.name", microLocationName);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.latitude", latitude);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.longitude", longitude);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.floor", floor);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.room", room);

        String idOfLocation = getValueFromJsonPath(responseData, "$.data.id");
        System.setProperty("Microlocation-id", idOfLocation);
    }


    @Test(priority = 1, description = "Delete a single microlocation")
    public void deleteMicroLocation() {
        String apiData = String.format(getPropertyByKey("delete.micro.location"), System.getProperty("Microlocation-id"));
        Response response = jerseyRequestForAPIInfo(apiData);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        verifyResponseFromJsonPath(response.readEntity(String.class), "$.meta.message", "Object successfully deleted");
    }
}
