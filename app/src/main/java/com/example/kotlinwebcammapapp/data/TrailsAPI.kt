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
//import com.example.kotlinwebcammapapp.BuildConfig

/**
 * Handles API requests for fetching webcam data from the prescriptiontrails.org API.
 * Object to handle the Trail API
 */
object TrailsAPI {
    // no api key needed
//    private val apiKey = BuildConfig.webcamApiKey

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
        val url = buildBaseURL(lat, lon)
        println("DEBUG API URL: $url")

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        return try {
            val response: HttpResponse = client.get(url) {
                expectSuccess = true
//                headers {
//                    append("Content-Type", "application/json")
//                    append("x-windy-api-key", apiKey)
//                }
            }

            if (response.status.value in 200..299) {
                val responseBody = response.bodyAsText()
                json.decodeFromString<TrailResponse>(responseBody)
            } else {
                println("DEBUG API Error: ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("DEBUG API Exception: ${e.message}")
            null
        } finally {
            client.close()
        }
    }

    /**
     * Builds the API URL based on given latitude and longitude.
     * @param lat Latitude for the request
     * @param lon Longitude for the request
     * @return String representing the built URL
     */
    private fun buildBaseURL(lat: Double, lon: Double): String {
//        return "https://api.windy.com/webcams/api/v3/webcams?limit=20&offset=0&categoryOperation=or&nearby=${lat},${lon},250&include=categories,images,location,player,urls&categories=city,traffic,forest,mountain,beach,harbor,bay,coast,golf,lake"
        return "https://prescriptiontrails.org/api/filter/?by=coord&lat=${lat}&lng=${lon}&offset=0&count=20"
    }
}
