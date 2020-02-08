package com.automation.api.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static com.automation.api.utils.PropertyUtils.getPropertyByKey;

public class RequestUtils {

    static String requestType, endPoint, contnetType, jsonBody;

    public static Response jerseyRequestForAPIKey(String apiInfo) {
        apiJsonParse(getPropertyByKey(apiInfo));
        Client client = ClientBuilder.newClient().property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
        WebTarget target = client.target(getPropertyByKey("base.url"));
        Response response = null;
        if (requestType.equalsIgnoreCase("GET")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).get(Response.class);
        } else if (requestType.equalsIgnoreCase("POST")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).post(Entity.entity(jsonBody, contnetType));
        } else if (requestType.equalsIgnoreCase("DELETE")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).delete();
        } else if (requestType.equalsIgnoreCase("PUT")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).put(Entity.entity(jsonBody, contnetType));
        } else {
            System.err.println("NOT VALID CHOICE");
        }
        return response;
    }

    /**
     * This method request to given api info for dynamic provided data
     *
     * @param apiInfo
     * @return
     */

    public static Response jerseyRequestForAPIInfo(String apiInfo) {
        apiJsonParse(apiInfo);
        Client client = ClientBuilder.newClient().property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
        WebTarget target = client.target(getPropertyByKey("base.url"));
        Response response = null;
        if (requestType.equalsIgnoreCase("GET")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).get(Response.class);
        } else if (requestType.equalsIgnoreCase("POST")) {
            response = target.path(endPoint).request().accept("application/vnd.api+json").header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).post(Entity.entity(jsonBody, contnetType));
        } else if (requestType.equalsIgnoreCase("DELETE")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).delete();
        } else if (requestType.equalsIgnoreCase("patch")) {
            response = target.path(endPoint).request().accept(contnetType).header("Authorization", "JWT " + System.getProperty("access_token")).header("Content-type", contnetType).build("PATCH", Entity.entity(jsonBody, contnetType)).invoke();
        } else {
            System.err.println("NOT VALID CHOICE");
        }
        return response;
    }

    /**
     * This Method will parse string to json and returns requestType, contnetType and jsonBody from provided string
     *
     * @param jsonString EX. {"endPoint":"/v1/event-types?sort=name","method":"post", "content-type":"application/vnd.api+json","body":{'\"data\"': {'\"attributes\"': {'\"name\"': '\"Eveny by Rest assure\"'},'\"type\"': '\"event-type\"'}}}
     */
    public static void apiJsonParse(String jsonString) {
        DocumentContext jsonContext;
        jsonContext = JsonPath.parse(jsonString);
        requestType = jsonContext.read("$.method").toString();
        contnetType = jsonContext.read("$.content-type").toString();
        jsonBody = jsonContext.read("$.body").toString().replaceAll("=", ":");
        endPoint = jsonContext.read("$.endPoint").toString();
    }
}

