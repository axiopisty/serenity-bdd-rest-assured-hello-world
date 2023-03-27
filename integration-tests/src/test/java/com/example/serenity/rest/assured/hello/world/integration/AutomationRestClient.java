package com.example.serenity.rest.assured.hello.world.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Optional;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AutomationRestClient {
  
  private static ObjectMapper objectMapper = new ObjectMapper();
  static {
    objectMapper.findAndRegisterModules();
  }
  
  public final static String dockerHost = readStringFromEnvVarOrDefault("DOCKER_HOST", "localhost");
  
  public final static int dockerPort = readIntegerFromEnvVarOrDefault("DOCKER_PORT", 8080);
  
  public static String readStringFromEnvVarOrDefault(String variableName, String defaultValue) {
    return ofNullable(getProperty(variableName)).map(String::trim).orElse(defaultValue);
  }
  
  public static Integer readIntegerFromEnvVarOrDefault(String variableName, Integer defaultValue) {
    return valueOf(readStringFromEnvVarOrDefault(variableName, String.valueOf(defaultValue)));
  }
  
  public static void configureBaseUri(Optional<Integer> port) {
    RestAssured.baseURI = format("http://%s:%d", dockerHost, port.orElse(dockerPort));
  }
  
  private Response getResponse;
  
  private Response postResponse;
  
  private Response putResponse;
  
  private Response patchResponse;
  
  private Response deleteResponse;
  
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public void invokeRequest(String method, String path, String jsonBody, Optional<Integer> port) {
    configureBaseUri(port);
    if ("GET".equals(method)) {
      getResponse = getJson(path);
    } else if ("POST".equals(method)) {
      postResponse = postJson(path, jsonBody);
    } else if ("PUT".equals(method)) {
      putResponse = putJson(path, jsonBody);
    } else if ("PATCH".equals(method)) {
      patchResponse = patchJson(path, jsonBody);
    } else if ("DELETE".equals(method)) {
      deleteResponse = deleteJson(path, jsonBody);
    } else {
      throw new IllegalStateException("Invalid test case. Method " + method + " is not supported");
    }
  }
  
  public Response getResponseForMethod(String method) {
    final Response response;
    if ("GET".equals(method)) {
      response = getResponse;
    } else if ("POST".equals(method)) {
      response = postResponse;
    } else if ("PUT".equals(method)) {
      response = putResponse;
    } else if ("PATCH".equals(method)) {
      response = patchResponse;
    } else if ("DELETE".equals(method)) {
      response = deleteResponse;
    } else {
      throw new IllegalStateException("Invalid test case. Method " + method + " is not supported");
    }
    return response;
  }
  
  public void assertResponseStatusCodeIs(String method, int expectedStatusCode) {
    final Response response = getResponseForMethod(method);
    assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
  }
  
  private Response getJson(String url) {
    return jsonRequest().get(url);
  }
  
  private Response postJson(String url, Object body) {
    final RequestSpecification requestSpecification = jsonRequest();
    return ofNullable(body)
      .map(requestSpecification::body)
      .orElse(requestSpecification)
      .post(url);
  }
  
  private Response putJson(String url, Object body) {
    return jsonRequest().body(body).put(url);
  }
  
  private Response patchJson(String url, Object body) {
    return given()
      .accept("*/*")
      .contentType("application/json-patch+json; charset=UTF-8")
      .urlEncodingEnabled(false)
      .body(body)
      .patch(url);
  }
  
  private Response deleteJson(String url, Object body) {
    final RequestSpecification requestSpecification = jsonRequest();
    return ofNullable(body)
      .map(requestSpecification::body)
      .orElse(requestSpecification)
      .delete(url);
  }
  
  private RequestSpecification jsonRequest() {
    return jsonRequest("application/json");
  }
  
  private RequestSpecification jsonRequest(String contentType) {
    return given().accept("application/json").contentType(contentType);
  }
  
}
