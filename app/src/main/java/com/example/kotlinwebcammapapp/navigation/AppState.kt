package com.example.kotlinwebcammapapp.navigation

import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.model.WebCam

/**
 * Sealed class representing different states/screens of the app.
 */
sealed class AppState {
    data object LocationInput : AppState()

    data class Map(
        val webcams: List<WebCam>,
        val trails: List<Trail>
    ) : AppState()

    data class WebCamList(
        val webcams: List<WebCam>,
        val trails: List<Trail>
    ) : AppState()

    data class TrailList(
        val webcams: List<WebCam>,
        val trails: List<Trail>
    ) : AppState()

    data class WebCamDetail(
        val webcam: WebCam,
        val webcamList: List<WebCam>,
        val trailList: List<Trail>
    ) : AppState()

    data class TrailDetail(
        val trail: Trail,
        val webcamList: List<WebCam>,
        val trailList: List<Trail>
    ) : AppState()

}
