package com.automation.api.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static com.automation.api.utils.PropertyUtils.getPropertyByKey;
import static com.automation.api.utils.PropertyUtils.loadProperties;
import static com.automation.api.utils.RequestUtils.jerseyRequestForAPIKey;

public class BaseTest {

    @BeforeClass
    public void beforeClassMethod() {
        loadProperties();
    }


    @Test(priority = 0)
    public void postJerseyRequest() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getPropertyByKey("base.url"));
        Response response = jerseyRequestForAPIKey("post.login.admin.user");

        String responseJson = response.readEntity(String.class);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseJson);
        Assert.assertEquals(response.getStatus(), 200);

        System.setProperty("access_token", String.valueOf(jsonObject.get("access_token")).replace("\"", ""));
    }

    public void getJerseyRequest() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getPropertyByKey("base.url"));
        Response response = target.path("/v1/users").request().accept("application/vnd.api+json").header("Authorization", "JWT " + System.getProperty("access_token")).get(Response.class);
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test(priority = 1)
    public void getUsersJerseyRequest() {
        Response response = jerseyRequestForAPIKey("get.users");
        Assert.assertEquals(response.getStatus(), 200);
    }
}

