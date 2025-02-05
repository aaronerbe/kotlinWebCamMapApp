package com.example.kotlinwebcammapapp.data

// Import necessary libraries
import com.example.kotlinwebcammapapp.model.Trail
import com.example.kotlinwebcammapapp.model.TrailResponse

/**
 * Trails class for managing trail data.
 * Calls TrailsAPI to fetch and store trail data.
 * @param lat Latitude for the request
 * @param lon Longitude for the request
 * @return Trails object containing trail data
 */
class Trails(private val lat: Double = 0.0, private val lon: Double = 0.0) {
    private var data: TrailResponse? = null // Stores API response

    /**
     * Initializes the WebCams object by fetching and parsing webcam data.
     */
    suspend fun init() {
        println("DEBUG TRAIL API INIT: Fetching data for $lat, $lon")
        // Calls the API to fetch webcam data and parse the response
        data = TrailsAPI.fetchTrailData(lat, lon)
        println("DEBUG TRAIL API Response: $data")
    }

    /**
     * Returns the list of webcams or an empty list if no data is available.
     * @return List<WebCam> containing webcam data
     */
    fun getTrails(): List<Trail> {
        return data?.trails?.values?.toList() ?: emptyList()
    }
}
