package com.automation.api.Tests;

import javax.ws.rs.core.Response;

import static com.automation.api.Data.STATUSCODE_CREATED;
import static com.automation.api.utils.CommonMethods.getValueFromJsonPath;
import static com.automation.api.utils.RequestUtils.jerseyRequestForAPIKey;
import static com.automation.api.utils.VerificationUtils.verifyResponseStatusCode;

public class CommonAPI {
    public static void createEvent() {
        Response response = jerseyRequestForAPIKey("post.create.event.body");
        verifyResponseStatusCode(STATUSCODE_CREATED, response);

        String responseData = response.readEntity(String.class);
        String idOfLocation = getValueFromJsonPath(responseData, "$.data.id");
        System.setProperty("event-id", idOfLocation);
    }
}
