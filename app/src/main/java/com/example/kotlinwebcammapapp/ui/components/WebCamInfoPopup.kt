package com.example.kotlinwebcammapapp.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.R
import com.example.kotlinwebcammapapp.model.WebCam
import coil3.compose.AsyncImage

/**
 * WebCam Info Popup Detail
 * @param webcam The webcam to display details for
 * @param onDismiss Function to dismiss the popup
 */
@Composable
fun WebCamInfoPopup(webcam: WebCam, onDismiss: () -> Unit) {
    // capture the context
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
                .clickable { onDismiss() }, // Click outside to dismiss
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Display the title and details
                Text(text = webcam.title, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Views: ${webcam.viewCount}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Status: ${webcam.status}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))

                // capture the url to the webcam daytime preview
                val imageUrl = webcam.images.daylight.preview

                // Display the image using AsyncImage addon.
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.default_image), // default image if it fails for some reason
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Clickable Windy URL
                Text(
                    text = "Windy URL",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcam.urls.detail))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Clickable Provider URL
                Text(
                    text = "Provider URL",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcam.urls.provider))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}
