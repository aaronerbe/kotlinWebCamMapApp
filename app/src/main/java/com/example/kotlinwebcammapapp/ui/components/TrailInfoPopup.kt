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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.utils.renderHtmlText

/**
 * Trail Info Popup Detail
 * @param trail The trail to display details for
 * @param onDismiss Function to dismiss the popup
 */
@Composable
fun TrailInfoPopup(
    trail: Trail,
    onDismiss: () -> Unit,
) {
    // Extract the activity type from the trail.  Used as part of the path to get the details later
    val activity = trail.activities.values.firstOrNull()?.activityTypeName

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
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                Text(text = renderHtmlText(trail.description), style = MaterialTheme.typography.bodySmall) // strips out the html tags so it looks nice
            }
        }
    }
}
