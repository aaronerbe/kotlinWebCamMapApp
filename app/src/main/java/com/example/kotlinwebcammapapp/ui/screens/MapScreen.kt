package com.example.kotlinwebcammapapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
//import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.ui.components.TrailInfoPopup
import com.example.kotlinwebcammapapp.ui.components.WebCamInfoPopup
import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.model.WebCam
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.example.kotlinwebcammapapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.kotlinwebcammapapp.ui.components.MultiChoiceSegmentedButton


@Composable
fun MapsScreen(
    webcamList: List<WebCam>,
    trailList: List<Trail>,
    onWebCamListViewClick: (List<WebCam>) -> Unit,
    onTrailsListViewClick: (List<Trail>) -> Unit,
    onTrailSelected: (Trail) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()
    var selectedWebCam by remember { mutableStateOf<WebCam?>(null) }
    var selectedTrail by remember { mutableStateOf<Trail?>(null) }

    // TODO for toggle filters
    //For toggling visibility of trails markers by activity
    var showHiking by remember {mutableStateOf(true)}
    var showCamping by remember {mutableStateOf(true)}
    var showMountainBiking by remember {mutableStateOf(true)}
    var showCaving by remember {mutableStateOf(true)}
    var showTrailRunning by remember {mutableStateOf(true)}
    var showSnowSports by remember {mutableStateOf(true)}
    var showATV by remember {mutableStateOf(true)}
    var showHorsebackRiding by remember {mutableStateOf(true)}
    val boundsBuilder = LatLngBounds.builder()
    val webCamMarkers = webcamList.map { webcam ->
        val position = LatLng(webcam.location.latitude, webcam.location.longitude)
        boundsBuilder.include(position)
        webcam to position
    }
    val trailMarkers = trailList.map { trail ->
        val position = LatLng(trail.lat, trail.lon)
        boundsBuilder.include(position)
        trail to position
    }

    //todo testing filters:

    // filter options.  if it's in this list, it will be displayed.  the multichoicesegemented button is used to toggle these in/out of the list.
//    var selectedOptions by remember { mutableStateOf(listOf("hiking", "camping", "caving", "mountain biking", "trail running", "snow sports", "atv", "horseback riding"))}
    var selectedOptions by remember { mutableStateOf(listOf("hiking", "camping", "caving", "mountain biking", "snow sports" ))}
    //todo, look at changing to bound based off trails.  or both?
    LaunchedEffect(webcamList) {
        if (webCamMarkers.isNotEmpty()) {
            val bounds = boundsBuilder.build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    selectedWebCam = null
                    selectedTrail = null
                }
            ) {
                webCamMarkers.forEach { (webcam, position) ->
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = webcam.title,
                        snippet = "Click for details",
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.webcam_marker5),
                        onClick = {
                            selectedWebCam = webcam
                            selectedTrail = null // Deselect any trail if a webcam is selected
                            true
                        }
                    )
                }

                trailMarkers.forEach { (trail, position) ->
                    val activity = trail.activities.values.firstOrNull()?.activityTypeName
                    // was val shouldShow = activity in selectedOptions but needed to lowercase it to account for variations
                    val shouldShow = activity?.lowercase() in selectedOptions.map { it.lowercase() }

                    println("DEBUG FILTER: $selectedOptions $activity $shouldShow")

                    //sets the icon that this marker will used based on the activity of the marker
                    val iconResourceId = when(activity){
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
                    if (shouldShow) {
                        Marker(
                            state = rememberMarkerState(position = position),
                            title = trail.name,
                            snippet = "Click for details",
                            icon = BitmapDescriptorFactory.fromResource(iconResourceId),
                            onClick = {
                                selectedTrail = trail
                                selectedWebCam = null
                                true
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
                    onDismiss = { selectedTrail = null },
                    onTrailNameSelected = { placeId ->
                        val selectedTrailDetail = trailList.firstOrNull { it.placeId == placeId }
                        selectedTrailDetail?.let { onTrailSelected(it) }
                    }
                )
            }


            // Floating Action Buttons
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MultiChoiceSegmentedButton(
                    selectedFilters = selectedOptions,
                    onFilterChange = { filter ->
                        selectedOptions = if (filter in selectedOptions) {
                            selectedOptions - filter // Remove if already selected
                        } else {
                            selectedOptions + filter // Add if not selected
                        }
                    },
                    modifier = Modifier.width(150.dp),
                )
                ExtendedFloatingActionButton(
                    modifier = Modifier.width(150.dp), // Set a fixed width (adjust as needed)
                    onClick = { onWebCamListViewClick(webcamList) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = { }, // Empty icon to prevent default centering
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Make Row take full width
                            horizontalArrangement = Arrangement.Start, // ✅ Left-align content
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Open WebCam List View")
                            Text("WebCams", modifier = Modifier.padding(start = 8.dp)) // Add spacing
                        }
                    }
                )

                ExtendedFloatingActionButton(
                    modifier = Modifier.width(150.dp), // Set the same width for both buttons
                    onClick = { onTrailsListViewClick(trailList) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = { }, // Empty icon to prevent default centering
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start, // ✅ Left-align content
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Open Trails List View")
                            Text("Trails", modifier = Modifier.padding(start = 8.dp)) // Add spacing
                        }
                    }
                )

            }
        }
    }
}
