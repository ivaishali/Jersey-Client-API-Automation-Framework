package com.automation.api.Tests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static com.automation.api.Data.*;
import static com.automation.api.utils.PropertyUtils.getPropertyByKey;
import static com.automation.api.utils.PropertyUtils.loadProperties;
import static com.automation.api.utils.RequestUtils.jerseyRequestForAPIKey;
import static com.automation.api.utils.VerificationUtils.verifyResponseJsonPathValue;
import static com.automation.api.utils.VerificationUtils.verifyResponseStatusCode;

public class EventScheduleAPIs {
    @BeforeClass
    public void beforeClassMethod() {
        loadProperties();
    }

    @Test(priority = 0, description = "Admin user login API test")
    public void postLoginApi() {
        Response response = jerseyRequestForAPIKey("post.login.admin.user");
        verifyResponseStatusCode(STATUSCODE_OK, response);
        String responseJson = response.readEntity(String.class);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseJson);
        Assert.assertEquals(response.getStatus(), 200);
        System.setProperty("access_token", String.valueOf(jsonObject.get("access_token")).replace("\"", ""));
    }

    @Test(priority = 1, description = "Creating new event-type API test")
    public void testPostCreateEventType() {
        Response response = jerseyRequestForAPIKey("post.create.event.type.body");
        verifyResponseStatusCode(STATUSCODE_CREATED, response);
        verifyResponseJsonPathValue(response, "data.attributes.name", getPropertyByKey("event.type"));
    }

    @Test(priority = 2, description = "Creating new event-topic API test")
    public void testCreateEventTopic() {
        Response response = jerseyRequestForAPIKey("post.create.event.topic.body");
        verifyResponseStatusCode(STATUSCODE_CREATED, response);
        verifyResponseJsonPathValue(response, "data.attributes.name", getPropertyByKey("event.topic"));
    }

    @Test(priority = 3, description = "Creating new event-subtopic API test")
    public void testCreateEventSubTopic() {
        Response response = jerseyRequestForAPIKey("post.create.event.sub.topic.body");
        verifyResponseStatusCode(STATUSCODE_CREATED, response);
        verifyResponseJsonPathValue(response, "data.attributes.name", getPropertyByKey("event.subtopic"));
    }

    @Test(priority = 4, description = "Creating new event API Test")
    public void testCreateAnEvent() {
        Response response = jerseyRequestForAPIKey("post.create.event.body");
        verifyResponseStatusCode(STATUSCODE_CREATED, response);
    }
}
