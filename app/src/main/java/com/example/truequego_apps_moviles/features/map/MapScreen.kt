package com.example.truequego_apps_moviles.features.map

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.domain.model.Product
import com.example.truequego_apps_moviles.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private val DEFAULT_CENTER = GeoPoint(4.5339, -75.6811)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val locationPermission = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
            osmdroidBasePath = context.filesDir
            osmdroidTileCache = context.cacheDir
        }
        if (!locationPermission.status.isGranted) {
            locationPermission.launchPermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        OsmMapView(
            context = context,
            products = products,
            hasLocationPermission = locationPermission.status.isGranted
        )

        // Header superpuesto
        Surface(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            color = SurfaceContainerLowest.copy(alpha = 0.95f),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp)) {
                Text(
                    "Mapa de intercambios",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
                Text(
                    when {
                        isLoading -> "Cargando productos..."
                        products.isEmpty() -> "Publica un producto para aparecer aquí"
                        else -> "${products.size} producto(s) en el mapa"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun OsmMapView(
    context: Context,
    products: List<Product>,
    hasLocationPermission: Boolean
) {
    // Guardamos referencias estables fuera del AndroidView
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }
    val locationOverlayRef = remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                MapView(ctx).also { map ->
                    map.setTileSource(TileSourceFactory.MAPNIK)
                    map.setMultiTouchControls(true)
                    map.controller.setZoom(14.0)
                    map.controller.setCenter(DEFAULT_CENTER)
                    mapViewRef.value = map
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                // Limpiamos solo los marcadores de productos, no el overlay de ubicación
                view.overlays.removeAll { it is Marker }

                // Creamos el overlay de ubicación solo una vez
                if (hasLocationPermission && locationOverlayRef.value == null) {
                    val overlay = MyLocationNewOverlay(
                        GpsMyLocationProvider(context), view
                    ).apply {
                        enableMyLocation()
                        enableFollowLocation()
                    }
                    view.overlays.add(0, overlay) // índice 0 = debajo de los marcadores
                    locationOverlayRef.value = overlay
                }

                // Añadimos marcadores de productos reales
                products.forEach { product ->
                    val marker = Marker(view).apply {
                        position = GeoPoint(product.latitude, product.longitude)
                        title = product.title
                        snippet = buildString {
                            if (product.condition.isNotBlank()) append(product.condition)
                            if (product.location.isNotBlank()) {
                                if (isNotEmpty()) append(" · ")
                                append(product.location)
                            }
                        }
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    view.overlays.add(marker)
                }

                view.invalidate()
            }
        )

        // Botón centrar en mi ubicación
        FloatingActionButton(
            onClick = {
                val myLoc = locationOverlayRef.value?.myLocation
                if (myLoc != null) {
                    mapViewRef.value?.controller?.animateTo(myLoc)
                    mapViewRef.value?.controller?.setZoom(17.0)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 24.dp),
            containerColor = PrimaryNavy,
            contentColor = OnPrimary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Mi ubicación")
        }

        // Leyenda
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            color = SurfaceContainerLowest.copy(alpha = 0.95f),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(10.dp), shape = CircleShape, color = TertiaryAccent) {}
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Productos", style = MaterialTheme.typography.labelSmall, color = OnSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(10.dp), shape = CircleShape, color = PrimaryNavy) {}
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tu ubicación", style = MaterialTheme.typography.labelSmall, color = OnSurface)
                }
            }
        }
    }

    // Limpieza al salir de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            locationOverlayRef.value?.disableMyLocation()
            locationOverlayRef.value = null
            mapViewRef.value?.onPause()
        }
    }
}
