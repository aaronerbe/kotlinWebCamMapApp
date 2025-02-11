package com.example.kotlinwebcammapapp.ui.screens

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.ui.components.TrailInfoPopup
import com.example.kotlinwebcammapapp.ui.components.WebCamInfoPopup
import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.model.WebCam
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.example.kotlinwebcammapapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.kotlinwebcammapapp.ui.components.MultiChoiceSegmentedButton

/**
 * MapsScreen Composable
 * Refactored to be the main screen.
 * Displays maps.  Initially centered on US.
 * Has tool bars above and below.
 *  Top Bar:  Enter Coordinates, Search This Area, Close App, Search by User Location, Webcam List, Trails List,
 *      Enter Coordinates:  Opens LocationInputScreen for user to enter/search coords.  Can also select use current location
 *      Search This Area:  Move map to area of interest.  Clicking this will search based on center of visible map
 *      Close App (X):  Closes the app
 *      Search by User Location (location icon):  Moves map to current location set by device and will search webcams/trails
 *      Webcam List (icon):  Opens WebCamListScreen.  Shows all searched webcams in list format
 *      Trail List (icon):  Opens TrailListScreen.  Shows all searched trails in list format
 *  Bot Bar:  Trail and Webcam visibility filters
 *  @param webcamList Input List of WebCam objects.  Created at search and passed in to display
 *  @param trailList Input List of Trail objects.  Created at search and passed in to display
 *  @param onWebCamListViewClick Callback to handle opening WebCamListScreen.  Function lives in GeoViewerApp (navigation)
 *      onWebCamListViewClick:  simply changes screen to WebCamListScreen and saves the webcam and trail states
 *  @param onTrailsListViewClick Callback to handle opening TrailListScreen.  Function lives in GeoViewerApp (navigation)
 *      onTrailsListViewClick:  simply changes screen to TrailListScreen and saves the webcam and trail states
 *  @param isLoading:  isLoading state passed in from WebCamApp to track when new searches happen and used to give spinner till it's loaded
 *  @param onSearch:  Callback to handle searching from within Map (Search This Area).  Grabs lat/lon from center of map and passes to GeoViewerApp which resubmits to MapScreen (new) with new coordinates
 *  @param onLocationInputClick:  Callback to navigate to the LocationInputScreen.  Function lives in GeoViewerApp (navigation).  Simply calls LocationInputScreen
 *  @param onUserLocationSearch:  Callback to handle searching from within Map (Search by User Location).  Grabs lat/lon from center of map and passes to GeoViewerApp which resubmits to MapScreen (new) with new coordinates
 *
 */
@Composable
fun MapsScreen(
    webcamList: List<WebCam>,  // input List of webcams to display
    trailList: List<Trail>,  // input List of trails to display
    onWebCamListViewClick: (List<WebCam>) -> Unit, // Callback to handle opening WebCamListScreen.  Function lives in GeoViewerApp (navigation)
    onTrailsListViewClick: (List<Trail>) -> Unit, // Callback to handle opening TrailListScreen.  Function lives in GeoViewerApp (navigation)
    isLoading: Boolean, // isLoading state passed in from WebCamApp to track when new searches happen and used to give spinner till it's loaded
    onSearch: (Double, Double) -> Unit, // Callback to handle searching from within Map
    onLocationInputClick: () -> Unit, // Callback to navigate to the LocationInputScreen
    onUserLocationSearch: () -> Unit // Callback to handle searching from within Map
) {
    val context = LocalActivity.current as Activity  // Activity needed to close app
    val cameraPositionState = rememberCameraPositionState() // Map Camera position
    var selectedWebCam by remember { mutableStateOf<WebCam?>(null) } // State to track selected webcam
    var selectedTrail by remember { mutableStateOf<Trail?>(null) } // State to track selected trail
    var lat by remember { mutableDoubleStateOf(0.0) } // Search Button provided latitude
    var lon by remember { mutableDoubleStateOf(0.0) } // Search Button provided longitude
    val boundsBuilder = LatLngBounds.builder()  // Initializes a builder to calculate the map bounds based on marker positions.

    // Generate a list of webcam markers with their positions.
    val webCamMarkers = webcamList.map { webcam ->
        val position = LatLng(webcam.location.latitude, webcam.location.longitude) // Convert webcam coordinates into a LatLng object.
        boundsBuilder.include(position) // Include this position in the bounds to ensure it fits within the visible map area.
        webcam to position // Return a pair containing the webcam object and its position.
    }

    // Generate a list of trail markers with their positions.
    val trailMarkers = trailList.map { trail ->
        val position = LatLng(trail.lat, trail.lon) // Convert trail coordinates into a LatLng object.
        boundsBuilder.include(position) // Include this position in the bounds to ensure it fits within the visible map area.
        trail to position  // Return a pair containing the trail object and its position.
    }

    // Filter options for toggling visibility.  needs to pair with the icon in the button
    var selectedOptions by remember { mutableStateOf(listOf("hiking", "camping", "mountain biking", "snow sports", "webcam")) }

    // If loading map for first time and no markers exist, center on the USA
    LaunchedEffect(webcamList, trailList) {
        if (webcamList.isEmpty() && trailList.isEmpty()) { // if empty (no markers exist)
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(LatLng(39.8283, -98.5795), 4f)) // set center to USA
        }
    }

    // This automatically updates the map's camera position whenever webcamList or trailList changes.
    // It ensures that all markers are visible by adjusting the map bounds.
    LaunchedEffect(webcamList, trailList) {
        if (webcamList.isNotEmpty() || trailList.isNotEmpty()) {
            // If at least one webcam or trail exists, adjust the map's viewport.
            val bounds = LatLngBounds.builder()  // Create a LatLngBounds.Builder to calculate the bounding box for all markers.
            // Add each webcam's position to the bounds.
            webcamList.forEach { bounds.include(LatLng(it.location.latitude, it.location.longitude)) }
            // Add each trail's position to the bounds.
            trailList.forEach { bounds.include(LatLng(it.lat, it.lon)) }
            // Move the camera to fit all included markers with 100px padding around the edges.
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        // Setup Column for the entire screen
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color= Color.Black),
            ) {
            // Top Button Section - Fixed at the Top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black)
                    .padding(12.dp)
                    .windowInsetsPadding(WindowInsets.statusBars),  // make sure it respects the status bar
                contentAlignment = Alignment.Center
            ) {
                // Place Top Section Buttons in a Row with 3 Columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Distribute buttons evenly
                ) {
                    // Column 1 (Webcam List & Trail List)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Button 1 (Row 1, Column 1) - Webcam List
                        SmallFloatingActionButton(
                            onClick = { onWebCamListViewClick(webcamList) }, // Callback to handle opening WebCamListScreen
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.webcam_white),
                                contentDescription = "Opens Webcams List",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Button 2 (Row 2, Column 1) - Trail List
                        SmallFloatingActionButton(
                            onClick = { onTrailsListViewClick(trailList) }, // Callback to handle opening TrailListScreen
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.trail_white),
                                contentDescription = "Opens Trails List",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Column 2 (Enter Coordinates & Search This Area)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Button 3 (Row 1, Column 2) - Enter Coordinates
                        Button(
                            onClick = { onLocationInputClick() },  // Callback to navigate to the LocationInputScreen
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Enter coordinate")
                        }

                        // Button 4 (Row 2, Column 2) - Search This Area
                        Button(
                            onClick = {  // Callback to handle searching from within Map  TODO:  combine this w/ onUserLocationSearch
                                val centerCoordinates = cameraPositionState.position.target
                                lat = centerCoordinates.latitude
                                lon = centerCoordinates.longitude
                                onSearch(lat, lon)
                            },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Search this area")
                        }
                    }

                    // Column 3 (Empty Slot & Search by User Location)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Button (Row 1, Column 3) - Close Button
                        SmallFloatingActionButton(
                            onClick = { finishAffinity(context) }, // Closes the app based on context (which is the LocalActivity)
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.close_white),
                                contentDescription = "Close App",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        // Button 5 (Row 2, Column 3) - Search by User Location
                        SmallFloatingActionButton(
                            onClick = { onUserLocationSearch() },  // Callback to handle searching by device location TODO:  combine with onSearch
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.location_white),
                                contentDescription = "Search by User Location",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Map Section - Takes Most of the Screen
            Box(modifier = Modifier.weight(1f)) {
                if (isLoading) { // if isLoading = true, show a blank screen with spinning progress indicator while content loads.  once it loads, this state will flip to false
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator() // Show a loading spinner
                    }
                } else { // if not isLoading, then show the map
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = {
                            selectedWebCam = null  // state for tracking which webcam is selected
                            selectedTrail = null  // state for tracking which trail is selected
                        },
                    ) {
                        // Add webcam markers using webCamMarkers which we built above.  has pair of webcam and position
                        webCamMarkers.forEach { (webcam, position) ->
                            val activity = "webcam"
                            val shouldShow = activity.lowercase() in selectedOptions.map { it.lowercase() } // Check if the activity is in the selectedOptions filter bar.  if not, that means user deselected it
                            if (shouldShow) {  //if it is selected, add the marker, else skips each one (not loading any webcams on map)
                                Marker(
                                    state = rememberMarkerState(position = position),  // Set the marker's position
                                    title = webcam.title,  // Set the marker's title
                                    snippet = "Click for details",  // Set the marker's snippet
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.webcam_marker3), // custom marker icon
                                    onClick = {
                                        selectedWebCam = webcam  // Set selectedWebCam to the clicked webcam if clicked
                                        selectedTrail = null // Deselect any trail if a webcam is selected
                                        true // Return true to indicate this is done
                                    }
                                )
                            }
                        }

                        // Add trail markers
                        trailMarkers.forEach { (trail, position) ->  // Pair of trail and position
                            val activity = trail.activities.values.firstOrNull()?.activityTypeName // get the activity name of the trail
                            val shouldShow = activity?.lowercase() in selectedOptions.map { it.lowercase() } //determine if it should be shown.  show if activity is in the selectedOptions (from filter bar)
                            val iconResourceId = when (activity) { // Define the icon based on the activity type
                                "hiking" -> R.drawable.hiking
                                "camping" -> R.drawable.camping
                                "mountain biking" -> R.drawable.mountain_biking
                                "caving" -> R.drawable.caving
                                "trail running" -> R.drawable.trail_running
                                "snow sports" -> R.drawable.snow_sports
                                "atv" -> R.drawable.atv
                                "horseback riding" -> R.drawable.horseback
                                else -> R.drawable.default_marker
                            }
                            if (shouldShow) {  //if it is selected, add the marker, else skips each one (not loading any trails on map)
                                Marker(
                                    state = rememberMarkerState(position = position), // Set the marker's position
                                    title = trail.name, // Set the marker's title
                                    snippet = "Click for details", // Set the marker's snippet
                                    icon = BitmapDescriptorFactory.fromResource(iconResourceId), // Set the marker's icon
                                    onClick = {
                                        selectedTrail = trail  // Set selectedTrail to the clicked trail if clicked
                                        selectedWebCam = null  // deselect webcams if they were selected before.
                                        true // Return true to indicate this is done
                                    }
                                )
                            }
                        }
                    }

                    // Show WebCamInfoPopup when a webcam is selected
                    selectedWebCam?.let { webcam ->
                        WebCamInfoPopup(
                            webcam = webcam,
                            onDismiss = { selectedWebCam = null }
                        )
                    }

                    // Show TrailInfoPopup when a trail is selected
                    selectedTrail?.let { trail ->
                        TrailInfoPopup(
                            trail = trail,
                            onDismiss = { selectedTrail = null }
                        )
                    }
                }
            }

            // Bottom Button Bar Section - Fixed at the Bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(color= Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // MultiChoiceSegmentedButton - This is the filter bar that feeds into the markers as they're built to determine if they're shown
                MultiChoiceSegmentedButton(
                    selectedFilters = selectedOptions, // List of selected filters
                    onFilterChange = { filter ->
                        selectedOptions = if (filter in selectedOptions) {
                            selectedOptions - filter // Remove if already selected
                        } else {
                            selectedOptions + filter // Add if not selected
                        }
                    },
                )
            }
        }
    }
}

//TODO disable trail filter buttons that don't have any activities already
//TODO  combine the search functions (search by user location just grab the location, then feed to same function as onSearch)
