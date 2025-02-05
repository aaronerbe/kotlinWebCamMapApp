package com.example.kotlinwebcammapapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrailResponse(
    val trails: Map<String, Trail>? = null // A map where keys are trail IDs and values are Trail objects
)

@Serializable
data class Trail(
    val name: String,
    val city: String,
    val state: String,
    val country: String,
    val description: String,
    val directions: String,
    val lat: Double,
    val lon: Double,
    @SerialName("parent_id") val parentId: String,
    @SerialName("place_id") val placeId: Long,
    val activities: Map<String, Activity> // A map where keys are activity types (e.g., "hiking") and values are Activity objects
)

@Serializable
data class Activity(
    val url: String,
    val length: String,
    val description: String,
    val name: String,
    val rank: String,
    val rating: String,
    val thumbnail: String,
    @SerialName("activity_type") val activityType: String,
    @SerialName("activity_type_name") val activityTypeName: String,
    val attribs: Attributes,
    @SerialName("place_activity_id") val placeActivityId: String
)

@Serializable
data class Attributes(
    val length: String
)
