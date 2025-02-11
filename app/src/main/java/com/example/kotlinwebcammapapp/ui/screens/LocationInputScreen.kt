package com.example.kotlinwebcammapapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.model.Coordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.kotlinwebcammapapp.R

/**
 * Location Input Screen
 * Allows the user to input a latitude and longitude.
 * @param getCoordinates Function to get the user's location
 * @param onLocationSubmit Function to submit the latitude and longitude
 * @param onBack Function to handle back navigation
 */
@Composable
fun LocationInputScreen(
    getCoordinates: suspend () -> Coordinates?, // Passed in Function from MainActivity to get user-provided coordinates
    onLocationSubmit: (Double, Double) -> Unit, // Function to submit user-provided latitude and longitude
    onBack: () -> Unit
) {
    // State variables for latitude and longitude input fields
    var lat by remember { mutableStateOf("") } // User provided latitude as a string
    var lon by remember { mutableStateOf("") } // User provided longitude as a string

    // State variables  for tracking validation of data
    var isLatError by remember { mutableStateOf(false) } // Flag for invalid latitude input
    var isLonError by remember { mutableStateOf(false) } // Flag for invalid longitude input

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        // Layout container for input fields and buttons
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the available screen space
                .padding(16.dp), // Add padding around the content
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            Text(
                "Enter Latitude and Longitude",
                style = MaterialTheme.typography.headlineMedium, // Apply headline style
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center, // Center the text
                modifier = Modifier.fillMaxWidth() // Make the text fill the available width
                    .padding(bottom = 32.dp) // Add padding below the text
            )

            Row () {
                Spacer(modifier = Modifier.weight(1f)) // Fills available space

                // Back button
                Button(
                    onClick = onBack,
    //                modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Back to Map")
                }
            }

            // Input field for latitude
            OutlinedTextField(
                value = lat, // Current value of the latitude input
                onValueChange = {
                    lat = it // Update the latitude value
                    isLatError = false // Reset the error flag
                },
                label = { Text("Latitude - e.g. 43.5") }, // Placeholder text
                isError = isLatError, // Highlight the field if there's an error
                modifier = Modifier.fillMaxWidth(), // Make the input field full-width
                shape = MaterialTheme.shapes.small // Small rounded corners
            )
            // Show an error message if the latitude is invalid
            if (isLatError) {
                Text(
                    "Invalid latitude. Must be between -90 and 90.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add spacing between fields

            // Input field for longitude
            OutlinedTextField(
                value = lon, // Current value of the longitude input
                onValueChange = {
                    lon = it // Update the longitude value
                    isLonError = false // Reset the error flag
                },
                label = { Text("Longitude - e.g. -116") }, // Placeholder text
                isError = isLonError, // Highlight the field if there's an error
                modifier = Modifier.fillMaxWidth(), // Make the input field full-width
                shape = MaterialTheme.shapes.small
            )
            // Show an error message if the longitude is invalid
            if (isLonError) {
                Text(
                    "Invalid longitude. Must be between -180 and 180.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add spacing between fields

            Row ()
            {
                Spacer(modifier = Modifier.weight(1f)) // Fills available space

                // Button to submit the latitude and longitude
                Button(
                    onClick = {
                        val latValue = lat.toDoubleOrNull() // Convert latitude to Double
                        val lonValue = lon.toDoubleOrNull() // Convert longitude to Double

                        // Validate the latitude value
                        if (latValue == null || latValue !in -90.0..90.0) {
                            isLatError = true
                        }

                        // Validate the longitude value
                        if (lonValue == null || lonValue !in -180.0..180.0) {
                            isLonError = true
                        }

                        // Submit the values if both are valid
                        if (!isLatError && !isLonError && latValue != null && lonValue != null) {
                            onLocationSubmit(latValue, lonValue)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Use primary color
                        contentColor = MaterialTheme.colorScheme.onPrimary // Use onPrimary for contrast
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Fetch Data") // Button label
                }

                // Button 1 (Row 1, Column 1) - Webcam List
                SmallFloatingActionButton(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val coordinates = getCoordinates() // Suspend function call
                            withContext(Dispatchers.Main) {
                                if (coordinates != null) {
                                    lat = coordinates.latitude.toString()
                                    lon = coordinates.longitude.toString()
                                    println("DEBUG LOCATION INPUT: $lat, $lon")
                                    onLocationSubmit(coordinates.latitude, coordinates.longitude)
                                } else {
                                    println("DEBUG LOCATION INPUT: Coordinates are null or permissions denied.")
                                }
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location_white),
                        contentDescription = "Use Current Location",
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        }
    }
}
