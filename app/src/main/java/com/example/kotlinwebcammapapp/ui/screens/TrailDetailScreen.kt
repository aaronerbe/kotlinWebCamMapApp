package com.example.kotlinwebcammapapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.model.Trail

/**
 * Trail Detail Screen
 * Displays detailed information about a selected trail, including title, location, and URLs.
 * Provides navigation back to the list screen.
 * @param trail Selected webcam to display
 * @param onBack Function to handle back navigation
 */
@Composable
fun TrailDetailScreen(
    trail: Trail, // Webcam details to display
    onBack: () -> Unit // Function to handle back navigation
) {
    val context = LocalContext.current // Provides access to the current context

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Layout container for the detail view
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the available screen space
                .padding(16.dp) // Add padding around the content
        ) {
            // Add spacing before the title
            Spacer(modifier = Modifier.height(62.dp))

            // Title for the screen
            Text(
                "Trail Details",
                style = MaterialTheme.typography.headlineMedium, // Apply headline style
                color = MaterialTheme.colorScheme.onBackground,
            )

            // Add spacing between title and details
            Spacer(modifier = Modifier.height(16.dp))

            // Display webcam details (title, location)
            Text(
                text = "Title: ${trail.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground // Use appropriate contrast color
            )
            Text(
                text = "Location: ${trail.city}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Description: ${trail.description}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Directions: ${trail.directions}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Add spacing between details and URLs
            Spacer(modifier = Modifier.height(16.dp))

            // Button to open the Windy URL
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trail.activities["hiking"]?.url))
                    context.startActivity(intent) // Open the URL in a browser
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium, // Apply consistent rounded corners
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Use secondary color for this button
                    contentColor = MaterialTheme.colorScheme.onSecondary // Use contrast color
                )
            ) {
                Text("Trail URL")
            }

            // Add spacing between buttons
            Spacer(modifier = Modifier.height(16.dp))

            // Button to open the provider URL
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trail.activities["hiking"]?.url))
                    context.startActivity(intent) // Open the URL in a browser
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Trail URL")
            }

            // Add spacing between URL buttons and the back button
            Spacer(modifier = Modifier.height(16.dp))

            // Back button to navigate back to the list screen
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium, // Consistent rounded corners for the back button
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Use primary color for the back button
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Back to List") // Label for the back button
            }
        }
    }
}