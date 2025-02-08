package com.example.kotlinwebcammapapp.utils

import com.example.kotlinwebcammapapp.data.Trails
import com.example.kotlinwebcammapapp.data.WebCams
import kotlinx.coroutines.runBlocking

/**
 * Builds a WebCams object using the given latitude and longitude.
 * This is a blocking operation. Meaning it forces i
 * @param lat Latitude for the request
 * @param lon Longitude for the request
 * @return WebCams object containing webcam data
 */
fun buildWebCamData(lat: Double, lon: Double): WebCams = runBlocking {
    val webCams = WebCams(lat, lon) // Create a WebCams object with the given coordinates
    webCams.init() // Fetch data from the API and initialize the object
    webCams // Return the initialized object
}

/**
 * Builds a Trails object using the given latitude and longitude.
 * This is a blocking operation for simplicity in this learning context.
 * @param lat Latitude for the request
 * @param lon Longitude for the request
 * @return Trail object containing trail data
 */
fun buildTrailData(lat: Double, lon: Double): Trails = runBlocking {
    val trails = Trails(lat, lon) // Create a Trails object with the given coordinates
    trails.init() // Fetch data from the API and initialize the object
    trails // Return the initialized object
}

