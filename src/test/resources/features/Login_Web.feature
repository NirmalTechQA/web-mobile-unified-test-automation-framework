# This is a sample feature file for validating the login functionality in an Web application.

@login @sanityWeb

Feature: Validating Login Feature in Web

Scenario Outline: Login should be successful with valid Email and OTP
    Given Enter the valid email "<email>"
	And Click on Request OTP
	Given Enter the OTP "<otp>"
	And Click on Submit OTP
	Then Verify Home page is displayed
	
	Examples:
	|email				|otp	|
	|nirmalk@gmail.com	|099909	|