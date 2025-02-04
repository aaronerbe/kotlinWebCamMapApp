package com.example.kotlinwebcammapapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.kotlinwebcammapapp.ui.theme.KotlinWebCamMapAppTheme
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.maps.android.compose.Polyline

// for floating button:
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.rememberMarkerState


class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinWebCamMapAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MapsScreen()
                }
            }
        }
    }
}

@Composable
fun MapsScreen() {
    val singapore = LatLng(1.35, 103.87)
    val jakarta = LatLng(-6.21, 106.85)

    val singaporeMarkerState = rememberMarkerState(position = singapore)
    val jakartaMarkerState = rememberMarkerState(position = jakarta)

    val context = LocalContext.current
    var selectedMarker by remember { mutableStateOf<LatLng?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Google Map (Full-Screen)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(singapore, 5f)
                },
                onMapClick = {
                    selectedMarker = null // ✅ Clicking anywhere outside closes the popup
                }
            ) {
                Marker(
                    state = singaporeMarkerState,
                    title = "Singapore",
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.web_cam_marker_png),
                    onClick = {
                        selectedMarker = singapore
                        true
                    }
                )
                Marker(
                    state = jakartaMarkerState,
                    title = "Jakarta",
                    snippet = "Click for more info",
                    onClick = {
                        selectedMarker = jakarta
                        true
                    }
                )
                Polyline(
                    points = listOf(singapore, jakarta),
                    color = Color.Blue
                )
            }

            // ✅ Show InfoPopup only when a marker is selected
            selectedMarker?.let { location ->
                InfoPopup(
                    location = location,
                    title = if (location == singapore) "Singapore" else "Jakarta",
                    description = "Click here to open more info.",
                    link = "https://example.com",
                    onDismiss = { selectedMarker = null } // Clicking anywhere inside popup closes it
                )
            }

            // Floating Action Button
            ExtendedFloatingActionButton(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Open List View Button.") },
                text = {
                    Text(
                        text = "Open List View",
                        color = MaterialTheme.colorScheme.onPrimary // Text color
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary, // Background color
                contentColor = MaterialTheme.colorScheme.onPrimary, // Text & icon color
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .padding(bottom = 30.dp)
            )
        }
    }
}

@Composable
fun InfoPopup(
    location: LatLng,
    title: String,
    description: String,
    link: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
                .clickable { // Clicking anywhere outside dismisses it
                    onDismiss()
                },
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        // Clicking the info card itself does nothing
                    }
            ) {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                // Clickable Link
                Text(
                    text = "Open More Info",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}
