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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.kotlinwebcammapapp.ui.components.MultiChoiceSegmentedButton

@Composable
fun MapsScreen(
    webcamList: List<WebCam>,
    trailList: List<Trail>,
    onWebCamListViewClick: (List<WebCam>) -> Unit,
    onTrailsListViewClick: (List<Trail>) -> Unit,
    isLoading: Boolean, // isLoading state passed in from WebCamApp to track when new searches happen and used to give spinner till it's loaded
    onSearch: (Double, Double) -> Unit, // Callback to handle searching from within Map
    onLocationInputClick: () -> Unit, // Callback to navigate to the LocationInputScreen
    onUserLocationSearch: () -> Unit // Callback to handle searching from within Map
) {
    val context = LocalActivity.current as Activity

    val cameraPositionState = rememberCameraPositionState()
    var selectedWebCam by remember { mutableStateOf<WebCam?>(null) }
    var selectedTrail by remember { mutableStateOf<Trail?>(null) }
    var lat by remember { mutableDoubleStateOf(0.0) } // Search Button provided latitude
    var lon by remember { mutableDoubleStateOf(0.0) } // Search Button provided longitude

    // TODO for toggle filters
    // For toggling visibility of trails markers by activity
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

    // Filter options for toggling visibility
    var selectedOptions by remember { mutableStateOf(listOf("hiking", "camping", "mountain biking", "snow sports", "webcam")) }

    // If loading map for first time and no markers exist, center on the USA
    LaunchedEffect(webcamList, trailList) {
        if (webcamList.isEmpty() && trailList.isEmpty()) {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(LatLng(39.8283, -98.5795), 4f))
        }
    }

    // Trying to clear map state on new search
    LaunchedEffect(webcamList, trailList) {
        if (webcamList.isNotEmpty() || trailList.isNotEmpty()) {
            val bounds = LatLngBounds.builder()
            webcamList.forEach { bounds.include(LatLng(it.location.latitude, it.location.longitude)) }
            trailList.forEach { bounds.include(LatLng(it.lat, it.lon)) }

            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
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
                            onClick = { onWebCamListViewClick(webcamList) },
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
                            onClick = { onTrailsListViewClick(trailList) },
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
                            onClick = { onLocationInputClick() },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Enter coordinate")
                        }

                        // Button 4 (Row 2, Column 2) - Search This Area
                        Button(
                            onClick = {
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
                            onClick = { finishAffinity(context) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.close_white),
                                contentDescription = "Search by User Location",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        // Button 5 (Row 2, Column 3) - Search by User Location
                        SmallFloatingActionButton(
                            onClick = { onUserLocationSearch() },
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
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator() // Show a loading spinner
                    }
                } else {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = {
                            selectedWebCam = null
                            selectedTrail = null
                        }
                    ) {
                        // Add webcam markers
                        webCamMarkers.forEach { (webcam, position) ->
                            val activity = "webcam"
                            val shouldShow = activity.lowercase() in selectedOptions.map { it.lowercase() }
                            if (shouldShow) {
                                Marker(
                                    state = rememberMarkerState(position = position),
                                    title = webcam.title,
                                    snippet = "Click for details",
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.webcam_marker3),
                                    onClick = {
                                        selectedWebCam = webcam
                                        selectedTrail = null // Deselect any trail if a webcam is selected
                                        true
                                    }
                                )
                            }
                        }

                        // Add trail markers
                        trailMarkers.forEach { (trail, position) ->
                            val activity = trail.activities.values.firstOrNull()?.activityTypeName
                            val shouldShow = activity?.lowercase() in selectedOptions.map { it.lowercase() }
                            val iconResourceId = when (activity) {
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
                            onDismiss = { selectedTrail = null }
                        )
                    }
                }
            }

            // Bottom Button Section - Fixed at the Bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(color= Color.Black),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                // ✅ MultiChoiceSegmentedButton (RESTORED)
                MultiChoiceSegmentedButton(
                    selectedFilters = selectedOptions,
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