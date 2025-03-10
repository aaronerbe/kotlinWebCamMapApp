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
import com.example.kotlinwebcammapapp.utils.renderHtmlText

/**
 * Trail List Screen
 * @param trails List of trails to display
 * @param onTrailNameSelected Callback to handle when a trail name is selected.  Sends to TrailDetailScreen
 * @param onBack Callback to handle back navigation
 */
@Composable
fun TrailListScreen(
    trails: List<Trail>,
    onTrailNameSelected: (Long) -> Unit = {},
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(62.dp))

            // Title for the screen
            Text(
                "Available Trails",
                style = MaterialTheme.typography.headlineMedium,
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

            // Scrollable list of trails
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Display each trail in the list
                trails.forEach { trail ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onTrailNameSelected(trail.placeId) }
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                trail.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Description:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = renderHtmlText(trail.description), // strips out the html tags so it looks nice
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}