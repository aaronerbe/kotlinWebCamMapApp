package com.example.kotlinwebcammapapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrailResponse( // Represents the top-level JSON object
    val countReturned: Int,  // Number of trails returned in the response
    val totalMatched: Int,   // Total number of trails that match the request
    val trails: List<Trail>  // List of Trail objects
)

@Serializable
data class Trail( // Represents an individual trail and its details
    val id: Int,         // Unique identifier for the trail
    val name: String,    // Name of the trail
    val city: String,    // City where the trail is located
    val zip: Int,        // Zip code of the trail location
    val crossstreets: String, // Cross streets for reference
    val address: String, // Address of the trailhead
    val transit: String, // Public transit options to the trail
    val lat: String,     // Latitude coordinate of the trail
    val lng: String,     // Longitude coordinate of the trail
    val desc: String,    // Description of the trail
    val lighting: String, // Type of lighting available
    val difficulty: Int, // Difficulty level of the trail
    val surface: String, // Surface type of the trail
    val parking: String, // Parking details
    val facilities: String, // Available facilities on-site
    val hours: String,   // Operating hours of the trail
    val loopcount: Int,  // Number of loops available
    val satImgURL: String, // URL for satellite image of the trail
    val largeImgURL: String, // URL for large image of the trail
    val thumbURL: String, // URL for thumbnail image of the trail
    val attractions: List<String>, // List of trail attractions
    val loops: Map<String, Loop>, // Nested object containing loops information
    val published: String, // Indicates if the trail is published
    val rating: Int,       // Average rating of the trail
    val ratings: Int,      // Number of ratings received
    val favorites: Int,    // Number of times the trail was favorited
    @SerialName("ModifiedTime")
    val modifiedTime: String, // Timestamp of last modification
    val reviews: Int,         // Number of reviews
    val distance: Double,     // Distance from the user's location
    val url: String           // URL to the trail's detail page
)

@Serializable
data class Loop( // Represents a specific loop within a trail
    val name: String,    // Name of the loop
    val distance: String, // Distance of the loop in miles
    val steps: Int       // Estimated number of steps to complete the loop
)

