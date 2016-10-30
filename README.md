# EstimoteAdapterBezirk

Android app for sending and receiving events for the Candid Camera Hackathon project using the Bezirk IoT Middleware.

## Project Installation
1. Clone this repository
2. Open this project in Android Studio and do a clean build: 'build -> rebuild project'
3. Install build tools, if you receive a build tools missing error. The link to install should be provided with the error. 
4. Adjust the Estimote beacon ID: `private static final String estimoteBeaconID` with your personal Estimote beacon ID in `src/main/java/com/bezirk/adapter/estimote/MainActivity.java`
5. Run application on your android device with `bluetooth, location and WIFI` enabled

## Running the Project
Since this is a distributed application, make sure all of the applications are connected to the same WIFI before running the application. 
