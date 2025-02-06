package com.example.kotlinwebcammapapp.navigation

import androidx.activity.compose.BackHandler
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

@Composable
fun WebCamApp(getCoordinates: suspend () -> Coordinates?) {
    var currentScreen by remember { mutableStateOf<AppState>(AppState.LocationInput) }

    BackHandler {
        when (currentScreen) {
            is AppState.WebCamList -> currentScreen = AppState.Map(
                (currentScreen as AppState.WebCamList).webcams,
                (currentScreen as AppState.WebCamList).trails
            )
            is AppState.TrailList -> currentScreen = AppState.Map(
                (currentScreen as AppState.TrailList).webcams,
                (currentScreen as AppState.TrailList).trails
            )
            is AppState.Map -> currentScreen = AppState.LocationInput
            else -> {}
        }
    }

    when (val state = currentScreen) {
        is AppState.LocationInput -> LocationInputScreen(
            getCoordinates = getCoordinates,
            onLocationSubmit = { lat, lon ->
                CoroutineScope(Dispatchers.IO).launch {
                    // buildWebCamData returns WebCams object which has a getter method .getWebCams
                    val webCams = buildWebCamData(lat, lon).getWebcams()
                    val trails = buildTrailData(lat, lon).getTrails()

                    withContext(Dispatchers.Main) {
                        currentScreen = AppState.Map(webCams, trails)
                    }
                }
            }
        )
        is AppState.Map -> MapsScreen(
            webcamList = state.webcams,
            trailList = state.trails,
            onWebCamListViewClick = { currentScreen = AppState.WebCamList(state.webcams, state.trails) },
            onTrailsListViewClick = { currentScreen = AppState.TrailList(state.webcams, state.trails) },
            onTrailSelected = { selectedTrail -> currentScreen = AppState.TrailDetail(selectedTrail, state.webcams, state.trails) }
        )
        is AppState.WebCamList -> WebCamListScreen(
            webcams = state.webcams,
            trails = state.trails,
            onWebCamSelected = { webcamId ->
                state.webcams.firstOrNull { it.webcamId == webcamId }?.let {
                    currentScreen = AppState.WebCamDetail(it, state.webcams, state.trails)
                }
            },
            onBack = { currentScreen = AppState.Map(state.webcams, state.trails) }
        )
        is AppState.TrailList -> TrailListScreen(
            trails = state.trails,
            webcams = state.webcams,
            onTrailNameSelected = { placeId ->
                state.trails.firstOrNull { it.placeId == placeId }?.let {
                    currentScreen = AppState.TrailDetail(it, state.webcams, state.trails)
                }
            },
            onBack = { currentScreen = AppState.Map(state.webcams, state.trails) }
        )
        is AppState.WebCamDetail -> WebCamDetailScreen(
            webcam = state.webcam,
            onBack = { currentScreen = AppState.WebCamList(state.webcamList, state.trailList) }
        )
        is AppState.TrailDetail -> TrailDetailScreen(
            trail = state.trail,
            onBack = { currentScreen = AppState.TrailList(state.webcamList, state.trailList) }
        )
    }
}
