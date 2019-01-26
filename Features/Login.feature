Feature: Login Action

  Scenario: Successful Login with Valid Credentials
    Given Open browser and naviagte to site and click on signin button
    When Enter "yourname@gmail.com" and "password"
    Then Validate the Username in the homepage
