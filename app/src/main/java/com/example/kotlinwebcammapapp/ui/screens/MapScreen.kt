package com.example.kotlinwebcammapapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = trail.name,
                        snippet = "Click for details",
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.webcam_marker3),
                        onClick = {
                            selectedTrail = trail
                            selectedWebCam = null // Deselect any webcam if a trail is selected
                            true
                        }
                    )
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { onWebCamListViewClick(webcamList) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Open WebCam List View") },
                    text = { Text("WebCam List") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )

                ExtendedFloatingActionButton(
                    onClick = { onTrailsListViewClick(trailList) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Open Trails List View") },
                    text = { Text("Trails List") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
