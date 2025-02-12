# Overview

## Background:
This is a program I used to learn Kotlin and Android App development and then implement GIS Mapping to really take it to the next level of interaction. 
### Stage 1:  Kotlin CLI
Stage 1 was a simple command line program written in Kotlin.  It allowed user to enter coordinates, then it returned a list of webcams in that area.  The user could then select a Webcam based on it's ID.  Finally it would return the details of that webcam including links to live video feed of the webcam.  This uses an API from WindyAPI.com.  
### Stage 2:  Android App
Stage 2 took this and build a simple and clean Android App.  Essentually the same functionality but in GUI form.  It compresed of 3 screens.  A Location Input Screen, A WebCam List Screen and a WebCam Detail Screen.
### Stage 3:  GIS Mapping
This is Stage 3 of the progress.  I have refactored the app to now utilize a Google Map for full interaction with the user. 
- The main screen of the app is a google map.
- There are 2 data APIs I am pulling:
  - WebCam API (from windyapi.com)
  - Trails API (from trailapi using rapidapi)
- There are 3 main ways to search for webcam and trail markers:
  - Enter Coordinates:  This will open LocationInputScreen allowing user to enter coordinates manually or click the location button to grab the coordinates of the device (with permission)
  - Search this area:  This will automatically pull the center of the visible map and search using those coordinates.  This allows the user to pan and explore using the map itself to find locations of interest.
  - Location Search (icon):  This will grab the coordinates of the device and search using those.
- There are 3 different ways to view the data results
  - Popup Markers on the Map:  Each data point will have a unique icon that marks the location.  Clicking will open a popup with details about the trail or webcam
    - The WebCam popup will also display a thumbnail of the webcam's 'daylight' image showing what the webcam is looking at under daylight.  
  - Trail List (icon):  This will open the TrailListScreen showing a full list with summaries of the trails searched
    - clicking on any trail will open the TrailDetailScreen showing the full details of the trail.
  - WebCam List (icon):  This will open the WebCamListScreen showing a full list with summaries of the webcams searched.  
    - clicking on any webcam will open the WebCamDetailScreen showing the full details of the webcam, including links to the live webcam feeds.
- On the bottom bar there are 5 filters to help filter visible data on the map.  Each trail type has a unique marker which is associated with the filter button.  Webcams all share the same icon - also associated with a filter button.
- A close button is given to close the app directly from within the Map.  

[Software Demo Video](http://youtube.link.goes.here)

# Development Environment

Tools used to develop this app are:
- Android Studio IDE
- Kotlin
- Windy API
- Trail API (Rapid API Host)

Libraries and Dependencies:
- Jetpack Compose Libraries
- Coroutine Libraries
- Location and Permission Libraries
- Ktor Client
- Kotlin Serialization
- Material Design Components
- Google Maps SDK
- COIL for creating images based on urls
- NavController for compose navigation
- Secret for obscuring API keys

# Useful Websites

{Make a list of websites that you found helpful in this project}
* [Segmented Button](https://developer.android.com/develop/ui/compose/components/segmented-button)
* [Secret Plugin](https://github.com/google/secrets-gradle-plugin)
* [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk)
* [Google Cloud Project](https://developers.google.com/maps/documentation/android-sdk/cloud-setup)
* [Google Maps API Setup](https://console.cloud.google.com/google/maps-apis/home;onboard=true?project=exalted-entity-449701-c9)
* [Floating Action Buttons](https://github.com/android/snippets/blob/a7117c0da26b85a9e005d700a7ae9dec859bb8bd/compose/snippets/src/main/java/com/example/compose/snippets/components/FloatingActionButton.kt#L83-L92)
* [Free Icons](https://www.iconfinder.com/)
* [Trail API](https://rapidapi.com/trailapi/api/trailapi)
* [Windy API](https://windyapi.com)

# Future Work

{Make a list of things that you need to fix, improve, and add in the future.}
* The Trail API is not providing clean data.  It should be giving links to pictures as well but they links are broken.  I'd like to either fix this or replace the API with cleaner data source
* I originally had a vision of pulling in Marina data but the Marina API is deprecate.  I'd like to find an alternative source for this and pull that in.
* I think the icons and toolbars could use some visual polishing.  
