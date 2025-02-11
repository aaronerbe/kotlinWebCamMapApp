package com.example.kotlinwebcammapapp.data

// Import necessary libraries
import com.example.kotlinwebcammapapp.model.TrailResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
// had to add this import to my Main before it would show up here?
import com.example.kotlinwebcammapapp.BuildConfig
import com.example.kotlinwebcammapapp.model.Trail

/**
 * Trails API class for managing trail data.  Handles API requests for fetching webcam data from the prescriptiontrails.org API.
 * Object to handle the Trail API
 */
object TrailsAPI {
    // API key for TRAILS obscured using secret addon
    private const val APIKEY = BuildConfig.trailApiKey

    // JSON configuration for parsing API responses
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    /**
     * Fetches data from the API.
     * @param lat Latitude for the request
     * @param lon Longitude for the request
     * @return TrailResponse? (Null if API call fails)
     */
    suspend fun fetchTrailData(lat: Double, lon: Double): TrailResponse? {
        val url = buildBaseURL(lat, lon) // build the URL
        println("DEBUG TRAIL API URL: $url")

        // Create an HTTP client using the CIO engine
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        // Make an HTTP GET request to the specified URL.  capture it as response
        return try {
            val response: HttpResponse = client.get(url) {
                expectSuccess = true
                headers {
                    append("x-rapidapi-host", "trailapi-trailapi.p.rapidapi.com")
                    append("x-rapidapi-key", APIKEY)
                }
            }

            if (response.status.value in 200..299) {
                val responseBody = response.bodyAsText() // read the response body as text
                println("DEBUG TRAIL API Response: $responseBody")
                val trailsMap: Map<String, Trail> = json.decodeFromString(responseBody) // parse the response body into a map of trails
                TrailResponse(trailsMap) // return the map as a TrailResponse object using the TrailResponse model
            } else {
                println("DEBUG TRAIL API Error: ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("DEBUG TRAIL API Exception: ${e.message}")
            null
        } finally {
            client.close() // close the client
        }
    }

    /**
     * Builds the API URL based on given latitude and longitude.
     * @param lat Latitude for the request
     * @param lon Longitude for the request
     * @return String representing the built URL
     */
    private fun buildBaseURL(lat: Double, lon: Double): String {
        //limiting to hiking
        return "https://trailapi-trailapi.p.rapidapi.com/activity/?lat=${lat}&limit=40&lon=${lon}&radius=50"
    }
}

//    return "https://trailapi-trailapi.p.rapidapi.com/activity/?lat=${lat}&limit=25&lon=${lon}&radius=25&q-activities_activity_type_name_eq=${activity}"
