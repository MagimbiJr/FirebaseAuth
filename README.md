# FirebaseAuth

Simple app that can register a user into Firebase and then sign in the user.

## Focus of the app

The main focus of this app is a self challenge to create simple app from this [design] (https://dribbble.com/shots/10168862-trip-login-and-registration/attachments/2113903?mode=media) into life.

## What's inside the app

The app requires the authenticated user to access MainActivity.
The first screen contains two button, one for registration and the other is for sign in.
The registration screen takes user information and then send the verification link to their email and take user to the sign in screen.
Then user needs to click the verification link sent to his/her email to be verified in order to sign in otherwise he/she will not be able to sign in.
After user is successful verified and sign in then he/she will enter the main activity which is blank.
Inside the main activity user has an option to sign out by clicking the sign out option from the option menu and then he/she will be redirected to the login screen.
In case user didn't get the verification link he/she needs to click the resend verification link button inside sign in screen.