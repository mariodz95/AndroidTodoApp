package com.example.todoapp.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.todoapp.model.TodoViewModel
import com.example.todoapp.util.rememberMapViewWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

@Composable
fun MapScreen(todoViewModel: TodoViewModel, navController: NavHostController, fixed: Boolean) {
    // The MapView lifecycle is handled by this composable. As the MapView also needs to be updated
    // with input from Compose UI, those updates are encapsulated into the MapViewContainer
    // composable. In this way, when an update to the MapView happens, this composable won't
    // recompose and the MapView won't need to be recreated.
    val long = todoViewModel.longitude.value
    val lat = todoViewModel.latitude.value

    val mapView = rememberMapViewWithLifecycle()
    val materialBlue700= Color(0xFF1976D2)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar", color = Color.White) },
                backgroundColor = materialBlue700,
                navigationIcon = {
                    IconButton(onClick = {
                        todoViewModel.clearSavedLongitudeAndLatitude()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "", tint = Color.White)
                    }
                },
            )
                 },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(
            backgroundColor = materialBlue700,
            onClick = {
            todoViewModel.saveLastChosenLocation()
            navController.popBackStack()
        }){
            Icon(Icons.Filled.Done, "")
        } },
        content = { MapViewContainer(
            map = mapView,
            latitude = if(todoViewModel.lastChosenLatitude.value == 0.0) lat else todoViewModel.lastChosenLatitude.value.toString(),
            longitude = if(todoViewModel.lastChosenLongitude.value == 0.0) long else todoViewModel.lastChosenLongitude.value.toString(),
            saveLongitude = {todoViewModel.saveLongitude(it)},
            saveLatitude = {todoViewModel.saveLatitude(it)},
            fixed = fixed,
        ) },
    )
}


@Composable
fun MapViewContainer(
    map: MapView,
    latitude: String,
    longitude: String,
    saveLongitude: (Double) -> Unit,
    saveLatitude: (Double) -> Unit,
    fixed: Boolean,
) {

    val context = LocalContext.current

    AndroidView({ map }) { mapView ->
        // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
        mapView.getMapAsync { googleMap ->
            val position = LatLng(latitude.toDouble(), longitude.toDouble())
            var marker = googleMap.addMarker(MarkerOptions().position(position))

            if(fixed) {
                googleMap.uiSettings.isScrollGesturesEnabled = false
                googleMap.uiSettings.isZoomGesturesEnabled = false

                googleMap.setOnMapClickListener {
                    val uri: String = java.lang.String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", latitude.toFloat(), longitude.toFloat())
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    context.startActivity(intent)
                }
            }

            googleMap.setOnCameraMoveListener {
                marker.position = googleMap.cameraPosition.target
                saveLongitude(googleMap.cameraPosition.target.longitude)
                saveLatitude(googleMap.cameraPosition.target.latitude)
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f))
        }
    }
}