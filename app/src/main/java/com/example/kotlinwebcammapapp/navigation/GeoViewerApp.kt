package com.example.kotlinwebcammapapp.navigation

import androidx.compose.runtime.*
import com.example.kotlinwebcammapapp.model.*
import com.example.kotlinwebcammapapp.ui.screens.LocationInputScreen
import com.example.kotlinwebcammapapp.ui.screens.MapsScreen
import com.example.kotlinwebcammapapp.ui.screens.TrailDetailScreen
import com.example.kotlinwebcammapapp.ui.screens.TrailListScreen
import com.example.kotlinwebcammapapp.ui.screens.WebCamDetailScreen
import com.example.kotlinwebcammapapp.ui.screens.WebCamListScreen
import com.example.kotlinwebcammapapp.utils.buildTrailData
import com.example.kotlinwebcammapapp.utils.buildWebCamData
import kotlinx.coroutines.*

/**
 * App the controls navigation between the screens
 * @param getCoordinates Suspend function to get coordinates from location input screen.  Also handles location permission requests permissions if needed
 */
@Composable
fun GeoViewerApp(getCoordinates: suspend () -> Coordinates?) {
    // Set the starting screen to the Map screen with an empty list of webcams and trails
    var currentScreen by remember { mutableStateOf<AppState>(AppState.Map(emptyList(), emptyList())) }

    // Track loading state to display a loading spinner when fetching new data
    var isLoading by remember { mutableStateOf(false) }

    // Handle navigation between different screens
    when (val state = currentScreen) {

        // Screen for manually entering latitude and longitude
        is AppState.LocationInput -> LocationInputScreen(
            getCoordinates = getCoordinates,
            onLocationSubmit = { lat, lon ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Fetch webcam and trail data for the provided coordinates
                    val webCams = buildWebCamData(lat, lon).getWebcams()
                    val trails = buildTrailData(lat, lon).getTrails()

                    withContext(Dispatchers.Main) {
                        // Navigate to the map screen with the fetched data
                        currentScreen = AppState.Map(webCams, trails)
                    }
                }
            },
            onBack = { currentScreen = AppState.Map(emptyList(), emptyList()) } // Return to the map screen
        )

        // Screen displaying the map with webcams and trails
        is AppState.Map -> MapsScreen(
            webcamList = state.webcams,
            trailList = state.trails,
            onWebCamListViewClick = { currentScreen = AppState.WebCamList(state.webcams, state.trails) },
            onTrailsListViewClick = { currentScreen = AppState.TrailList(state.webcams, state.trails) },
            isLoading = isLoading, // Pass loading state to the map screen

            // Search for webcams and trails at the new map center
            onSearch = { lat, lon ->
                isLoading = true // Show loading spinner while fetching data
                CoroutineScope(Dispatchers.IO).launch {
                    val webCamsDeferred = async { buildWebCamData(lat, lon).getWebcams() }
                    val trailsDeferred = async { buildTrailData(lat, lon).getTrails() }

                    val webCams = webCamsDeferred.await()
                    val trails = trailsDeferred.await()

                    withContext(Dispatchers.Main) {
                        // Step 1: Clear the current map data
                        currentScreen = AppState.Map(emptyList(), emptyList())

                        // Step 2: Small delay to ensure recomposition
                        // TODO: See if this can be removed
                        // delay(100) // Uncomment if needed

                        // Step 3: Stop loading spinner
                        isLoading = false

                        // Step 4: Load the new data into the map
                        currentScreen = AppState.Map(webCams, trails)
                    }
                }
            },

            // Navigate to the location input screen
            onLocationInputClick = { currentScreen = AppState.LocationInput },

            // Search for webcams and trails based on the user's current location
            onUserLocationSearch = {
                isLoading = true // Show loading spinner while fetching data
                CoroutineScope(Dispatchers.IO).launch {
                    val coordinates = getCoordinates() // Fetch current location
                    val lat = coordinates?.latitude ?: 0.0
                    val lon = coordinates?.longitude ?: 0.0
                    val webCamsDeferred = async { buildWebCamData(lat, lon).getWebcams() }
                    val trailsDeferred = async { buildTrailData(lat, lon).getTrails() }

                    val webCams = webCamsDeferred.await()
                    val trails = trailsDeferred.await()

                    withContext(Dispatchers.Main) {
                        // Step 1: Clear the current map data
                        currentScreen = AppState.Map(emptyList(), emptyList())

                        // Step 2: Small delay to ensure recomposition
                        // TODO: See if this can be removed
                        // delay(100) // Small delay to force re-render

                        // Step 3: Stop loading spinner
                        isLoading = false

                        // Step 4: Load the new data into the map
                        currentScreen = AppState.Map(webCams, trails)
                    }
                }
            }
        )

        // Screen displaying a list of webcams
        is AppState.WebCamList -> WebCamListScreen(
            webcams = state.webcams,
            onWebCamSelected = { webcamId ->
                // Find the selected webcam and navigate to its details screen
                state.webcams.firstOrNull { it.webcamId == webcamId }?.let {
                    currentScreen = AppState.WebCamDetail(it, state.webcams, state.trails)
                }
            },
            onBack = { currentScreen = AppState.Map(state.webcams, state.trails) } // Return to the map screen
        )

        // Screen displaying a list of trails
        is AppState.TrailList -> TrailListScreen(
            trails = state.trails,
            onTrailNameSelected = { placeId ->
                // Find the selected trail and navigate to its details screen
                state.trails.firstOrNull { it.placeId == placeId }?.let {
                    currentScreen = AppState.TrailDetail(it, state.webcams, state.trails)
                }
            },
            onBack = { currentScreen = AppState.Map(state.webcams, state.trails) } // Return to the map screen
        )

        // Screen displaying details of a selected webcam
        is AppState.WebCamDetail -> WebCamDetailScreen(
            webcam = state.webcam,
            onBack = { currentScreen = AppState.WebCamList(state.webcamList, state.trailList) } // Return to the webcam list
        )

        // Screen displaying details of a selected trail
        is AppState.TrailDetail -> TrailDetailScreen(
            trail = state.trail,
            onBack = { currentScreen = AppState.TrailList(state.webcamList, state.trailList) } // Return to the trail list
        )
    }
}

