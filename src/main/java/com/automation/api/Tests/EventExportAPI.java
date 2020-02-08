package com.automation.api.Tests;

import com.automation.api.utils.RequestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static com.automation.api.Data.STATUSCODE_OK;
import static com.automation.api.utils.CommonMethods.loadPropertiesAndLoginUser;
import static com.automation.api.utils.PropertyUtils.getPropertyByKey;
import static com.automation.api.utils.VerificationUtils.verifyResponseFromJsonPath;
import static com.automation.api.utils.VerificationUtils.verifyResponseStatusCode;

public class EventExportAPI {
    @BeforeClass
    public void beforeClassMethod() {
        loadPropertiesAndLoginUser();
    }

    @Test(priority = 0, description = "START EVENT EXPORT AS ICAL FILE")
    public void testGetEventExportAsICAL() {
        Response response = RequestUtils.jerseyRequestForAPIKey("get.event.export.as.ical.file");
        verifyResponseStatusCode(STATUSCODE_OK, response);
    }

    @Test(priority = 1, description = "Update a single user by setting the email, email and/or name")
    public void testUpdateUserDetail() {
        String randomAppenderStr = RandomStringUtils.randomNumeric(4);

        String requestStr = String.format(getPropertyByKey("patch.user.detail.update"), randomAppenderStr, randomAppenderStr);
        Response response = RequestUtils.jerseyRequestForAPIInfo(requestStr);
        verifyResponseStatusCode(STATUSCODE_OK, response);
        String responseData = response.readEntity(String.class);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.first-name", randomAppenderStr);
        verifyResponseFromJsonPath(responseData, "$.data.attributes.last-name", randomAppenderStr);
    }
}
