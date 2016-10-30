# EstimoteAdapterBezirk

This is an Android app for sending and receiving events for the Candid Camera Hackathon project using the Bezirk IoT Middleware.

## Project Installation
1. Clone this repository
2. Open this project in Android Studio and do a clean build: 'build -> rebuild project'
3. If you receive a "build tools missing" error, click the link in the error to install additional Android build tools.
4. In `src/main/java/com/bezirk/adapter/estimote/MainActivity.java`, where `private static final String estimoteBeaconID` is set, change the beacon ID to match the beacon you have.  For Estimote beacons, this can be found via the beacon controller app. 
5. Run the application on all authorized android devices (i.e. smartphones of people expected to be coming and going) with bluetooth, and wifi enabled.  Some beacons may require the location feature to be enabled as well.
6. Install the Bluetooth beacon on or near the "inside" of the door.  You may want to use a temporary mounting at first to experiment with the exact placement later. 
7. The prototype app gives a visual indicator of an X when the beacon is not detected, and a checkmark when it is, on a delay from when the phone enters or leaves the beacon range.  Using this information can help you in adjusting the power level of the beacon, which should be relatively low in order to just cover the area around the doorway, while still producing alerts if e.g. a stranger enters while the authorized owner is asleep in a back room.  
8. To reduce signal delays, we also recommended increasing the beacon advertising frequency, closer to 100ms than 1000.  

