package com.example.truequego_apps_moviles.features.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.ui.theme.GrayText
import com.example.truequego_apps_moviles.ui.theme.LightBlueBg
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val step by viewModel.currentStep
    val nombre by viewModel.fullName
    val email by viewModel.email
    val password by viewModel.password
    val ciudad by viewModel.location
    val rango by viewModel.notificationRange

    // Para seleccionar ciudad
    var showCitySelector by remember { mutableStateOf(false) }
    val cities = listOf("Armenia, Quindío", "Calarcá, Quindío", "Montenegro, Quindío", "Circasia, Quindío", "La Tebaida, Quindío")

    LaunchedEffect(key1 = true) {
        viewModel.registerResult.collectLatest { message ->
            if (message == "Cuenta creada exitosamente") {
                onRegisterSuccess()
            } else {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                IconButton(onClick = { if (step == 1) onNavigateBack() else viewModel.onPreviousStep() }, modifier = Modifier.padding(8.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                LinearProgressIndicator(
                    progress = { if (step == 1) 0.5f else 1f },
                    modifier = Modifier.fillMaxWidth().height(4.dp).padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.LightGray
                )
                Text(
                    text = "Paso $step de 2",
                    modifier = Modifier.fillMaxWidth().padding(end = 24.dp, top = 8.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall,
                    color = GrayText
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            androidx.compose.animation.AnimatedContent(
                targetState = step,
                transitionSpec = {
                    (androidx.compose.animation.fadeIn() togetherWith androidx.compose.animation.fadeOut())
                },
                label = "RegisterStepAnimation"
            ) { targetStep ->
                if (targetStep == 1) {
                    StepOne(
                        nombre = nombre,
                        email = email,
                        password = password,
                        onNombreChange = { viewModel.fullName.value = it },
                        onEmailChange = { viewModel.email.value = it },
                        onPasswordChange = { viewModel.password.value = it },
                        onContinue = { viewModel.onNextStep() }
                    )
                } else {
                    StepTwo(
                        ciudad = ciudad,
                        rango = rango,
                        onCiudadChange = { viewModel.location.value = it },
                        onRangoChange = { viewModel.notificationRange.value = it },
                        onFinalize = { viewModel.onCreateAccount() },
                        onOpenCitySelector = { showCitySelector = true }
                    )
                }
            }
        }

        // Selector de ciudad (Dialog)
        if (showCitySelector) {
            AlertDialog(
                onDismissRequest = { showCitySelector = false },
                title = { Text("Selecciona tu ciudad") },
                text = {
                    Column {
                        cities.forEach { city ->
                            Text(
                                text = city,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.location.value = city
                                        showCitySelector = false
                                    }
                                    .padding(vertical = 12.dp),
                                fontSize = 16.sp,
                                color = if (ciudad == city) MaterialTheme.colorScheme.primary else Color.Black,
                                fontWeight = if (ciudad == city) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCitySelector = false }) { Text("CERRAR") }
                }
            )
        }
    }
}

@Composable
fun StepOne(
    nombre: String,
    email: String,
    password: String,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Únete a miles de personas que ya intercambian",
            style = MaterialTheme.typography.bodyLarge,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(40.dp))

        RegisterField(label = "NOMBRE COMPLETO", value = nombre, onValueChange = onNombreChange, placeholder = "Tu nombre completo")
        Spacer(modifier = Modifier.height(24.dp))
        RegisterField(label = "CORREO ELECTRÓNICO", value = email, onValueChange = onEmailChange, placeholder = "tu@correo.com")
        Spacer(modifier = Modifier.height(24.dp))
        RegisterField(label = "CONTRASEÑA", value = password, onValueChange = onPasswordChange, placeholder = "Mínimo 8 caracteres", isPassword = true)

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Continuar", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
fun StepTwo(
    ciudad: String,
    rango: String,
    onCiudadChange: (String) -> Unit,
    onRangoChange: (String) -> Unit,
    onFinalize: () -> Unit,
    onOpenCitySelector: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Tu ubicación",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Asi te mostramos los trueques más cercanos a ti",
            style = MaterialTheme.typography.bodyLarge,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text("CIUDAD", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Campo de Ciudad 100% interactivo
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onOpenCitySelector() },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = ciudad,
                    modifier = Modifier.weight(1f),
                    color = Color.Black
                )
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        // Mapa interactivo simulado
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onOpenCitySelector() },
            color = Color(0xFFE8F1F8),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Simulación de mapa con cuadrícula
                Column(modifier = Modifier.fillMaxSize()) {
                    repeat(10) { 
                        HorizontalDivider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Row(modifier = Modifier.fillMaxSize()) {
                    repeat(10) { 
                        Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White.copy(alpha = 0.3f)))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                
                // Pin del mapa animado (simulado)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.LocationOn, 
                        contentDescription = null, 
                        modifier = Modifier.size(48.dp), 
                        tint = Color.Red
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = ciudad,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                Text(
                    "Toca para cambiar ubicación", 
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
                    style = MaterialTheme.typography.bodySmall, 
                    color = GrayText
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("RANGO DE NOTIFICACIONES", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RangoButton("5 km", selected = rango == "5 km", onClick = onRangoChange, modifier = Modifier.weight(1f))
            RangoButton("10 km", selected = rango == "10 km", onClick = onRangoChange, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RangoButton("20 km", selected = rango == "20 km", onClick = onRangoChange, modifier = Modifier.weight(1f))
            RangoButton("50 km", selected = rango == "50 km", onClick = onRangoChange, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onFinalize,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Crear mi cuenta 🎉", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RegisterField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, isPassword: Boolean = false) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = { 
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, 
                            contentDescription = "Ver contraseña", 
                            tint = if (passwordVisible) MaterialTheme.colorScheme.primary else GrayText
                        )
                    }
                } 
            },
            singleLine = true
        )
    }
}

@Composable
fun RangoButton(text: String, selected: Boolean, onClick: (String) -> Unit, modifier: Modifier) {
    OutlinedButton(
        onClick = { onClick(text) },
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) LightBlueBg else Color.Transparent,
            contentColor = if (selected) MaterialTheme.colorScheme.primary else GrayText
        ),
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else Color.LightGray)
    ) {
        Text(text, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}
