@loginiOS @sanity
Feature: Validating Login Feature in iOS device

Scenario Outline: Login should be successful with valid Mobile Number and OTP in iOS
	Given Enter the valid mobile number "<mobileNo>"
	And Select the Valid country code
	And Click on Sign In button in iOS
	Given Enter the default OTP "<otp>" in iOS
	And Click on Verify button
	Then Verify App Home page is displayed in iOS
	
	Examples:
	|mobileNo		|otp		|
	|	9890999999	|	109099	|
