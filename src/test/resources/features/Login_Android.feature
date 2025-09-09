@loginandroid @sanity

Feature: Validating Login Feature in Android device

Background:
	Given Accept the app permission popup

Scenario Outline: Login should be successful with valid Mobile Number and OTP
	Given Enter the valid mobile number "<mobileNo>" along with country code
	And Click on Login button
	Given Enter the default OTP "<otp>"
	And Click on Submit button
	Then Verify App Home page is displayed
	
	Examples:
	|mobileNo		|otp		|
	|	9890999999	|	333490	|
