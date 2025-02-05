package com.example.kotlinwebcammapapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.model.Trail

@Composable
fun TrailInfoPopup(
    trail: Trail,
    onDismiss: () -> Unit,
    onTrailNameSelected: (Long) -> Unit // Ensure this is correctly passed from the parent
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable { onDismiss() }, // Dismiss when clicking outside
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Make the name clickable to navigate to details
                Text(
                    text = "Trail Name: ${trail.name}",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.clickable {
                        println("DEBUG: Trail clicked: ${trail.name} (ID: ${trail.placeId})")
                        onTrailNameSelected(trail.placeId) // Trigger state change
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Trail Description: ${trail.description}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Directions: ${trail.directions}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
