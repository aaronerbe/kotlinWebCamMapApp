package com.example.kotlinwebcammapapp

// Core Android Imports
import android.os.Bundle // Stores and transfers data between screens (activities)
import androidx.activity.ComponentActivity // Base class for activities (screens) that use Jetpack Compose
import androidx.activity.compose.setContent // Sets the Compose UI as the activity's content
// App specific imports
import com.example.kotlinwebcammapapp.model.Coordinates // import coordinates class
// Location permissions and compatibility imports
import android.Manifest // For requesting location permissions
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager // For checking permission status
import androidx.activity.result.contract.ActivityResultContracts // Manages permission requests
//import androidx.core.content.ContextCompat // Provides compatibility methods for permissions
import androidx.core.app.ActivityCompat // For permission handling
import com.google.android.gms.location.LocationServices // Provides location services
import com.google.android.gms.location.FusedLocationProviderClient // Accesses the device's location
import android.location.Location // Represents a location
import android.net.Uri
import android.provider.Settings
import android.widget.Toast // Displays short messages to the user
// UI utilities
import com.example.kotlinwebcammapapp.ui.theme.KotlinWebCamMapAppTheme
// Coroutine Utilities
import kotlin.coroutines.resume  //resume suspended coroutines
import kotlin.coroutines.suspendCoroutine  //suspend execution till resumed
// Main App
import com.example.kotlinwebcammapapp.navigation.WebCamApp


/**
 * MainActivity Class is the entry point of the app.
 * In here, I have code to verify permissions and fetch the user's location if requested.
 */
class MainActivity : ComponentActivity() {
    // FusedLocationProviderClient
    // provides access to location services for retrieving the device's location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FusedLocationProviderClient for accessing location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            // Apply the theme to the entire app
            KotlinWebCamMapAppTheme {
                /**
                 * Set the content of the activity to the WebCamApp composable.
                 * @param {getCoordinates()} Lambda to get the user's location
                 */
                WebCamApp(getCoordinates = { getCoordinates() })
                //TODO  Rename??
            }
        }
    }

    /**
     * Function for handling getting user coordinates
     * Handles checking permissions, calling launcher if needed and getting coordinates
     * Note:  suspend needed since it's an async call to get the coordinates.  Needed to avoid returning null before it was done actually getting the coordinates
     * Also, this function is unnecessary.  Could just go straight to fetchUserLocation().  May clean this up in next sprint.
     * @return CheckLocationPermissions() -> Coordinates?
     */
    private suspend fun getCoordinates(): Coordinates? {
        // return checkLocationPermission()
        // refactoring this to accommodate changes
        return suspendCoroutine { continuation ->
            fetchUserLocation { location ->
                continuation.resume(location)
            }
        }

//
//        return fetchUserLocation()
    }

    /**
     * Fetches the user's last known location using FusedLocationProviderClient.
     * This function is a suspend function that can only be called within a coroutine.
     * @return Coordinates? The user's last known coordinates or null if unavailable or permissions are missing.
     */
    //change this to use a callback so it returns and grabs the location after permissions granted
    //need to check if it locks it into a loop if user doesn't grant permissions
    //removing suspend
    private fun fetchUserLocation(onLocationFetched: (Coordinates?) -> Unit){
        // Check if the app has the necessary permissions for accessing location.  Required
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        if (ActivityCompat.checkSelfPermission(this,fineLocationPermission) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,coarseLocationPermission) != PackageManager.PERMISSION_GRANTED
        ){
            //Request permissions 1st, then try fetching location
            requestPermissionsLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
            return
        }
        // If permissions are granted, request the last known location from the FusedLocationProviderClient
        fusedLocationClient.lastLocation
            .addOnSuccessListener {location: Location? ->
                if (location != null) {
                    // If a location is successfully retrieved, extract latitude and longitude
                    val userLat = location.latitude
                    val userLon = location.longitude
                    println("DEBUG LOCATION INPUT: Latitude: $userLat, Longitude: $userLon")
                    // Resume the coroutine with the fetched coordinates
                    onLocationFetched(Coordinates(userLat, userLon))
                } else {
                    // If no location is available, log a message and resume with null
                    println("DEBUG LOCATION INPUT: Unable to fetch location.")
                    onLocationFetched(null)
                }
            }
            .addOnFailureListener { exception ->
                println("DEBUG LOCATION INPUT: Error retrieving location: ${exception.message}")
                onLocationFetched(null)
            }



        //TODO work out a way to suspend operation while we open permissions launcher.
        // Else 1st time running it, this code returns empty requiring user to click button again to actually pass permissions check and get location
        // It's only ever the first time so, meh

        // Suspend the coroutine while waiting for the result from the FusedLocationProviderClient
//        return suspendCoroutine { continuation ->
//            // Request the last known location from the FusedLocationProviderClient
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location: Location? ->
//                    if (location != null) {
//                        // If a location is successfully retrieved, extract latitude and longitude
//                        val userLat = location.latitude
//                        val userLon = location.longitude
//                        println("DEBUG LOCATION INPUT: Latitude: $userLat, Longitude: $userLon")
//                        // Resume the coroutine with the fetched coordinates
//                        continuation.resume(Coordinates(userLat, userLon))
//                    } else {
//                        // If no location is available, log a message and resume with null
//                        println("DEBUG LOCATION INPUT: Unable to fetch location.")
//                        continuation.resume(null)
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    // If there was an error retrieving the location, log the error and resume with null
//                    println("DEBUG LOCATION INPUT: Error retrieving location: ${exception.message}")
//                    continuation.resume(null)
//                }
//        }
    }

    //Handle sending to settings if they selected don't ask again for permissions and then click on it anyways
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Permission Required")
            .setMessage("Location access is required for this feature. Please enable location in settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Shows permission request launcher to handle the result of permission requests.
     * This will handle whether the user grants or denies the location permissions.
     */
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if either FINE or COARSE location permission was granted
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            println("DEBUG LOCATION INPUT: Permissions granted")
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()

            // permissions granted, so retry fetching location
            fetchUserLocation { location ->
                println("DEBUG LOCATION INPUT: Retried location fetched: $location")
            }
        } else {
            // Notify the user that permissions were denied
            Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show()
        }
    }
}