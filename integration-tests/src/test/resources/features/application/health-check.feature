Feature: Application Health Check Feature

  Scenario: Application should have a health check
    When GET /health
    Then the GET response status code should be 200 OK