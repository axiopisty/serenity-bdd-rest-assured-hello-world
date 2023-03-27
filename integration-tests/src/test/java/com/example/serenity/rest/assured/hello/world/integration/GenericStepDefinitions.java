package com.example.serenity.rest.assured.hello.world.integration;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;
import net.thucydides.core.annotations.Steps;

import static com.example.serenity.rest.assured.hello.world.integration.AutomationRestClient.dockerPort;
import static java.util.Optional.of;

public class GenericStepDefinitions {
  
  @Steps(shared = true)
  AutomationRestClient automationRestClient;
  
  @When("^(POST|PUT|GET|DELETE|PATCH) (/.+)$")
  public void whenMethodPath(String method, String path) {
    Optional<Integer> portOption = of(dockerPort);
    automationRestClient.invokeRequest(method, path, null, portOption);
  }
  
  @Then("^the (POST|PUT|GET|DELETE|PATCH) response status code should be (\\d+)(?: .+)?$")
  public void thenResponseStatusCodeShouldBe(String method, int expectedStatusCode) {
    automationRestClient.assertResponseStatusCodeIs(method, expectedStatusCode);
  }
  
}
