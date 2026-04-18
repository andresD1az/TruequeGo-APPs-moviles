package com.example.truequego_apps_moviles.features.publish

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.ui.component.PrimaryButton
import com.example.truequego_apps_moviles.ui.component.SoftFocusTextField
import com.example.truequego_apps_moviles.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PublishScreen(
    onNavigateBack: () -> Unit,
    viewModel: PublishViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val title by viewModel.title
    val description by viewModel.description
    val condition by viewModel.condition
    val imageUrl by viewModel.imageUrl
    val locationName by viewModel.locationName
    val isLoading by viewModel.isLoading

    // Solicitar permiso de ubicación automáticamente
    val locationPermission = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!locationPermission.status.isGranted) {
            locationPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.result.collectLatest { msg ->
            if (msg == "OK") onNavigateBack()
            else snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PrimaryNavy)
            }
        },
        containerColor = SurfaceContainerLowest
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Publicar producto",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )

            SoftFocusTextField(
                value = title,
                onValueChange = { viewModel.title.value = it },
                label = "TÍTULO",
                placeholder = "Ej: MacBook Pro 2021",
                enabled = !isLoading
            )

            SoftFocusTextField(
                value = description,
                onValueChange = { viewModel.description.value = it },
                label = "DESCRIPCIÓN",
                placeholder = "Describe el estado y características",
                singleLine = false,
                enabled = !isLoading
            )

            SoftFocusTextField(
                value = condition,
                onValueChange = { viewModel.condition.value = it },
                label = "ESTADO",
                placeholder = "Nuevo / Usado / Buen estado",
                enabled = !isLoading
            )

            SoftFocusTextField(
                value = locationName,
                onValueChange = { viewModel.locationName.value = it },
                label = "UBICACIÓN",
                placeholder = "Ej: Armenia, Quindío",
                trailingIcon = {
                    Icon(Icons.Default.LocationOn, null, tint = TertiaryAccent)
                },
                enabled = !isLoading
            )

            SoftFocusTextField(
                value = imageUrl,
                onValueChange = { viewModel.imageUrl.value = it },
                label = "URL DE IMAGEN (opcional)",
                placeholder = "https://...",
                enabled = !isLoading
            )

            if (locationPermission.status.isGranted) {
                Surface(
                    color = PrimaryContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, null, tint = PrimaryNavy)
                        Text(
                            "Tu ubicación GPS se guardará automáticamente para aparecer en el mapa.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryNavy
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Publicar",
                onClick = { viewModel.publish() },
                isLoading = isLoading,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}
