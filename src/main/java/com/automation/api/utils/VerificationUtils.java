package com.automation.api.utils;

import com.jayway.jsonpath.DocumentContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;

import javax.ws.rs.core.Response;


public class VerificationUtils {
    static Logger logger = LogManager.getLogger(VerificationUtils.class);

    public static <T> void assertThat(String msg, T actual, Matcher<? super T> matcher) {
        assertThat("", actual, matcher);
    }

    public static <T> void assertThat(String msg, String actual, Matcher<String> matcher) {
        if (!verifyThat(msg, actual, matcher)) {
            throw new AssertionError();
        }
        logger.log(Level.ALL, "Assertion is getting executed... !!");
    }

    public static <T> boolean verifyThat(String msgAssertion, String actual, Matcher<String> matcher) {
        boolean result = matcher.matches(actual);
        Description description = (Description) new StringDescription();
        description.appendText(msgAssertion).appendText("\nExpected: ").appendDescriptionOf(matcher)
                .appendText("\n     Actual: ");

        matcher.describeMismatch(actual, description);
        String msg = description.toString();
        if (msg.endsWith("Actual: ")) {
            msg = String.format(msg + "%s", actual);
        }
        msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        return result;
    }

    public static void verifyResponseStatusCode(String statusCode, Response response) {
        assertThat("Assert Response Status", String.valueOf(response.getStatus()), Matchers.equalToIgnoringCase(statusCode));
    }

    public static void verifyResponseJsonPathValue(Response response, String jsonPathKey, String actualValue) {
//        String expectedValue = response.(jsonPathKey).toString();
//        assertThat("Assert Response Status", expectedValue, Matchers.equalToIgnoringCase(actualValue));
    }

    public static void verifyResponseFromJsonPath(String responseData, String jsonPathKey, Object actualValue) {
        DocumentContext jsonContext;
        jsonContext = com.jayway.jsonpath.JsonPath.parse(responseData);
        String jsonPathValue = jsonContext.read(jsonPathKey).toString();
        if (actualValue instanceof String) {
            actualValue = actualValue.toString();
        }
        assertThat("Assert Response Status", jsonPathValue, Matchers.containsString(actualValue.toString()));
    }

    public static String getValueFromJsonPath(String responseData, String jsonPathKey) {
        DocumentContext jsonContext;
        jsonContext = com.jayway.jsonpath.JsonPath.parse(responseData);
        return jsonContext.read(jsonPathKey).toString();
    }
}
