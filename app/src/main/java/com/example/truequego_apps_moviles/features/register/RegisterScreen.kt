package com.example.truequego_apps_moviles.features.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.ui.component.PrimaryButton
import com.example.truequego_apps_moviles.ui.component.SoftFocusTextField
import com.example.truequego_apps_moviles.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val step by viewModel.currentStep
    val nombre by viewModel.fullName
    val email by viewModel.email
    val password by viewModel.password
    val ciudad by viewModel.location
    val rango by viewModel.notificationRange
    val isLoading by viewModel.isLoading

    LaunchedEffect(key1 = true) {
        viewModel.registerResult.collectLatest { message ->
            if (message == "REGISTER_SUCCESS") onRegisterSuccess()
        }
    }

    RegisterContent(
        step = step,
        nombre = nombre, email = email, password = password,
        ciudad = ciudad, rango = rango, isLoading = isLoading,
        onNombreChange = { viewModel.fullName.value = it },
        onEmailChange = { viewModel.email.value = it },
        onPasswordChange = { viewModel.password.value = it },
        onCiudadChange = { viewModel.location.value = it },
        onRangoChange = { viewModel.notificationRange.value = it },
        onNextStep = { viewModel.onNextStep() },
        onPreviousStep = { viewModel.onPreviousStep() },
        onFinalize = { viewModel.onCreateAccount() },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RegisterContent(
    step: Int,
    nombre: String, email: String, password: String,
    ciudad: String, rango: String, isLoading: Boolean,
    onNombreChange: (String) -> Unit, onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit, onCiudadChange: (String) -> Unit,
    onRangoChange: (String) -> Unit, onNextStep: () -> Unit,
    onPreviousStep: () -> Unit, onFinalize: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showCitySelector by remember { mutableStateOf(false) }
    val cities = listOf("Armenia, Quindío", "Calarcá, Quindío", "Montenegro, Quindío", "Circasia, Quindío", "La Tebaida, Quindío")

    Scaffold(
        topBar = {
            Column {
                IconButton(
                    onClick = { if (step == 1) onNavigateBack() else onPreviousStep() },
                    modifier = Modifier.padding(8.dp),
                    enabled = !isLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.register_navigate_back),
                        tint = PrimaryNavy
                    )
                }
                LinearProgressIndicator(
                    progress = { if (step == 1) 0.5f else 1f },
                    modifier = Modifier.fillMaxWidth().height(2.dp).padding(horizontal = 24.dp),
                    color = PrimaryNavy,
                    trackColor = SurfaceContainerHigh
                )
            }
        },
        containerColor = SurfaceContainerLowest
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp)
        ) {
            if (step == 1) {
                StepOne(
                    nombre = nombre, email = email, password = password,
                    onNombreChange = onNombreChange, onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onContinue = onNextStep, isLoading = isLoading
                )
            } else {
                StepTwo(
                    ciudad = ciudad, rango = rango,
                    onCiudadChange = onCiudadChange, onRangoChange = onRangoChange,
                    onFinalize = onFinalize, onOpenCitySelector = { showCitySelector = true },
                    isLoading = isLoading
                )
            }
        }

        if (showCitySelector) {
            AlertDialog(
                onDismissRequest = { showCitySelector = false },
                title = { Text(stringResource(R.string.register_city_dialog_title), color = PrimaryNavy) },
                text = {
                    Column {
                        cities.forEach { city ->
                            Text(
                                text = city,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCiudadChange(city); showCitySelector = false }
                                    .padding(vertical = 12.dp),
                                color = if (ciudad == city) PrimaryNavy else OnSurface
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCitySelector = false }) {
                        Text(stringResource(R.string.register_city_dialog_close))
                    }
                }
            )
        }
    }
}

@Composable
fun StepOne(
    nombre: String, email: String, password: String,
    onNombreChange: (String) -> Unit, onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit, onContinue: () -> Unit, isLoading: Boolean
) {
    Column {
        Spacer(modifier = Modifier.height(32.dp))
        Text(stringResource(R.string.register_title_step1), style = MaterialTheme.typography.displayMedium, color = PrimaryNavy)
        Spacer(modifier = Modifier.height(40.dp))
        SoftFocusTextField(
            label = stringResource(R.string.register_name_label),
            value = nombre, onValueChange = onNombreChange,
            placeholder = stringResource(R.string.register_name_placeholder),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        SoftFocusTextField(
            label = stringResource(R.string.register_email_label),
            value = email, onValueChange = onEmailChange,
            placeholder = stringResource(R.string.register_email_placeholder),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        SoftFocusTextField(
            label = stringResource(R.string.register_password_label),
            value = password, onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.register_password_placeholder),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(40.dp))
        PrimaryButton(text = stringResource(R.string.register_continue_button), onClick = onContinue, isLoading = isLoading)
    }
}

@Composable
fun StepTwo(
    ciudad: String, rango: String,
    onCiudadChange: (String) -> Unit, onRangoChange: (String) -> Unit,
    onFinalize: () -> Unit, onOpenCitySelector: () -> Unit, isLoading: Boolean
) {
    Column {
        Spacer(modifier = Modifier.height(32.dp))
        Text(stringResource(R.string.register_title_step2), style = MaterialTheme.typography.displayMedium, color = PrimaryNavy)
        Spacer(modifier = Modifier.height(40.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).clickable { onOpenCitySelector() },
            shape = RoundedCornerShape(8.dp),
            color = SurfaceContainerLow
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = PrimaryNavy)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = ciudad, color = OnSurface)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        PrimaryButton(text = stringResource(R.string.register_create_button), onClick = onFinalize, isLoading = isLoading)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    TruequeGoAPPsmovilesTheme {
        RegisterContent(
            step = 1, nombre = "", email = "", password = "", ciudad = "Armenia", rango = "5 km",
            isLoading = false, onNombreChange = {}, onEmailChange = {}, onPasswordChange = {},
            onCiudadChange = {}, onRangoChange = {}, onNextStep = {}, onPreviousStep = {},
            onFinalize = {}, onNavigateBack = {}
        )
    }
}
