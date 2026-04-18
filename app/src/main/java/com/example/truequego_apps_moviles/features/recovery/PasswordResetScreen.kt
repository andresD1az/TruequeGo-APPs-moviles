package com.example.truequego_apps_moviles.features.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.ui.component.PrimaryButton
import com.example.truequego_apps_moviles.ui.component.SoftFocusTextField
import com.example.truequego_apps_moviles.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.recoveryResult.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    PasswordResetContent(
        codeDigits = viewModel.codeDigits,
        newPassword = viewModel.newPassword.value,
        confirmNewPassword = viewModel.confirmNewPassword.value,
        onCodeDigitChange = { index, value, focusRequesters ->
            if (value.length <= 1) {
                viewModel.codeDigits[index] = value
                if (value.isNotEmpty() && index < 4) focusRequesters[index + 1].requestFocus()
            }
        },
        onNewPasswordChange = { viewModel.newPassword.value = it },
        onConfirmNewPasswordChange = { viewModel.confirmNewPassword.value = it },
        onResetClick = { viewModel.onResetPassword() },
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetContent(
    codeDigits: List<String>,
    newPassword: String,
    confirmNewPassword: String,
    onCodeDigitChange: (Int, String, List<FocusRequester>) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onResetClick: () -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val focusRequesters = remember { List(5) { FocusRequester() } }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.reset_navigate_back),
                            tint = PrimaryNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceContainerLowest)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = SurfaceContainerLowest
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.reset_title),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.reset_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                codeDigits.forEachIndexed { index, digit ->
                    OutlinedTextField(
                        value = digit,
                        onValueChange = { onCodeDigitChange(index, it, focusRequesters) },
                        modifier = Modifier.weight(1f).focusRequester(focusRequesters[index]),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceContainerLowest,
                            unfocusedContainerColor = SurfaceContainerLow,
                            focusedBorderColor = PrimaryNavy,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            SoftFocusTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                label = stringResource(R.string.reset_new_password_label),
                placeholder = stringResource(R.string.reset_new_password_placeholder),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = OnSurfaceVariant
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            SoftFocusTextField(
                value = confirmNewPassword,
                onValueChange = onConfirmNewPasswordChange,
                label = stringResource(R.string.reset_confirm_password_label),
                placeholder = stringResource(R.string.reset_confirm_password_placeholder),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            PrimaryButton(
                text = stringResource(R.string.reset_button),
                onClick = onResetClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PasswordResetPreview() {
    TruequeGoAPPsmovilesTheme {
        PasswordResetContent(
            codeDigits = listOf("", "", "", "", ""),
            newPassword = "", confirmNewPassword = "",
            onCodeDigitChange = { _, _, _ -> },
            onNewPasswordChange = {}, onConfirmNewPasswordChange = {},
            onResetClick = {}, onNavigateBack = {}
        )
    }
}
