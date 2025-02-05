package com.example.kotlinwebcammapapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.model.WebCam

/**
 * Webcam List Screen
 * Displays a scrollable list of webcams with a back button.
 * @param webcams List of webcams to display
 * @param onWebCamSelected Function to handle when a webcam is selected
 * @param onBack Function to handle back navigation
 */
@Composable
fun WebCamListScreen(
    webcams: List<WebCam>,
    trails: List<Trail>, // Add trails to be passed back to map when going back
    onWebCamSelected: (Long) -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Layout container for the webcam list
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the available screen space
                .padding(16.dp) // Add padding around the content
        ) {
            Spacer(modifier = Modifier.height(62.dp))

            // Title for the screen
            Text(
                "Available WebCams",
                style = MaterialTheme.typography.headlineMedium, // Apply headline style
                color = MaterialTheme.colorScheme.onBackground,
            )
            // Back button
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Back to Map")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable list of webcams
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                webcams.forEach { webcam ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface) // Surface background color
                            .clickable { onWebCamSelected(webcam.webcamId) }
                            .padding(16.dp) // Inner padding
                    ) {
                        Column {
                            Text(
                                webcam.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "ID: ${webcam.webcamId}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}