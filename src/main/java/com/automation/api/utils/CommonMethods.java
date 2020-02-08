package com.automation.api.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import org.testng.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static com.automation.api.utils.PropertyUtils.getPropertyByKey;
import static com.automation.api.utils.PropertyUtils.loadProperties;
import static com.automation.api.utils.RequestUtils.jerseyRequestForAPIKey;

public class CommonMethods {
    public static void loadPropertiesAndLoginUser() {
        loadProperties();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getPropertyByKey("base.url"));
        Response response = jerseyRequestForAPIKey("post.login.admin.user");
        String responseJson = response.readEntity(String.class);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseJson);
        Assert.assertEquals(response.getStatus(), 200);
        System.setProperty("access_token", String.valueOf(jsonObject.get("access_token")).replace("\"", ""));
    }

    public static String getValueFromJsonPath(String responseString, String jsonPathKey) {
        DocumentContext jsonContext;
        jsonContext = com.jayway.jsonpath.JsonPath.parse(responseString);
        return jsonContext.read(jsonPathKey).toString();
    }
}
