# Android CTF - FSB Messenger 

## Intro
Welcome to FSB Messenger, your hub for selling state secrets. 


This repo contains the challenge for an Android CTF. There are two challenges. 

1. Find the hardcoded password used for the login screen. 


2. Once you find the password and login, you're greeted with a chat interface where you can try and sell secrets. However, you'll need to bypass emulation detection to get it! 

## Demos 
Here are small demos of the app. 

### Demo 1 - No exploit (but I will log in to show chat interface)
![demo 1](/assets/demo1.gif)

### Demo 2 - Exploit loaded 
![demo 1](/assets/demo2.gif)

## Testing 

The workflow runs two different type of tests. To test the first part of the challenge (hardcoded secrets) it uses espresso UI tests to enter the password to verify that the decrypted password will actually work. There is an example python script that players will most likely use something similar to located [here](/pocs/login.py) that shows how to use python with the hardcoded aes key and encrypted password. 


The second test is more hacky, but first clears the ADB logs and reinstalls the APK to run Frida on it. Within the app the same emulator verification logic is used earlier and logs whether the device is an emulator or not. Then run grep on the log file and check if emulator is indeed false, indicating the exploit functions properly. That is located [here](/pocs/exploit.js). 

Both of these require the reactivecircus android emulator action to function. Documentation here: https://github.com/ReactiveCircus/android-emulator-runner 