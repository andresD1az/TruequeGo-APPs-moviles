package com.example.truequego_apps_moviles.features.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.ui.component.PrimaryButton
import com.example.truequego_apps_moviles.ui.component.SoftFocusTextField
import com.example.truequego_apps_moviles.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReset: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val email by viewModel.email

    LaunchedEffect(key1 = true) {
        viewModel.recoveryResult.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
            if (message == "RECOVERY_EMAIL_SENT") onNavigateToReset()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.recovery_navigate_back),
                    tint = PrimaryNavy
                )
            }
        },
        containerColor = SurfaceContainerLowest
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.size(120.dp),
                color = PrimaryContainer,
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = PrimaryNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.recovery_title),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.recovery_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            SoftFocusTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                label = stringResource(R.string.recovery_email_label),
                placeholder = stringResource(R.string.recovery_email_placeholder),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            PrimaryButton(
                text = stringResource(R.string.recovery_send_button),
                onClick = { viewModel.onSendRecoveryLink() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.recovery_spam_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
