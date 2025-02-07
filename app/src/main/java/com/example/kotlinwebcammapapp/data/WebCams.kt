package com.example.kotlinwebcammapapp.data

// Import necessary libraries
import com.example.kotlinwebcammapapp.model.WebCam
import com.example.kotlinwebcammapapp.model.WebCamResponse

/**
 * WebCams class for managing webcam data.
 * Calls WebCamAPI to fetch and store webcam data.
 * @param lat Latitude for the request
 * @param lon Longitude for the request
 * @return WebCams object containing webcam data
 */
class WebCams(private val lat: Double = 0.0, private val lon: Double = 0.0) {
    private var data: WebCamResponse? = null // Stores API response

    /**
     * Initializes the WebCams object by fetching and parsing webcam data.
     */
    suspend fun init() {
        println("DEBUG API INIT: Fetching data for $lat, $lon")
        // Calls the API to fetch webcam data and parse the response
        data = WebCamAPI.fetchWebCamData(lat, lon)
        println("DEBUG API Response: $data")
    }

    /**
     * Returns the list of webcams or an empty list if no data is available.
     * @return List<WebCam> containing webcam data
     */
    fun getWebcams(): List<WebCam> {
        return data?.webcams ?: emptyList()
    }
}
