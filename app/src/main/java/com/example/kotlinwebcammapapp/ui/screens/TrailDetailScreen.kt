package com.example.kotlinwebcammapapp.ui.screens

//import android.content.Intent
//import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.utils.renderHtmlText

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
//    val context = LocalContext.current // Provides access to the current context
    // Extract the activity type from the trail.  Used as part of the path to get the details later
    val activity = trail.activities.values.firstOrNull()?.activityTypeName

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Layout container for the detail view
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the available screen space
                .padding(16.dp) // Add padding around the content
                .verticalScroll(rememberScrollState())
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

            // Make the name clickable to navigate to details
            Text(text = trail.name,style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Activity Type: $activity", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Length: ${trail.activities[activity]?.length} miles", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Directions:", style = MaterialTheme.typography.bodyMedium)
            Text(text = renderHtmlText(trail.directions), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Trail Description:", style = MaterialTheme.typography.bodyMedium)
            renderHtmlText(trail.description) // strips out the html tags so it looks nice
            Text(text = renderHtmlText(trail.description), style = MaterialTheme.typography.bodySmall)


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