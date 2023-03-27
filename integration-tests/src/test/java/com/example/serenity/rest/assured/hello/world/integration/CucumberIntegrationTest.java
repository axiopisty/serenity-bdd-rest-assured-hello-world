package com.example.serenity.rest.assured.hello.world.integration;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
  features = "src/test/resources/features",
  glue = "com.example.serenity.rest.assured.hello.world.integration",
  plugin = {"pretty"}
  
)
public class CucumberIntegrationTest {
}
