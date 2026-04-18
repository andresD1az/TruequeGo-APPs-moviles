package com.example.truequego_apps_moviles.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.truequego_apps_moviles.ui.component.PrimaryButton
import com.example.truequego_apps_moviles.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceContainerLowest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Usamos Icons.Filled para asegurar compatibilidad con el set extendido
                Icon(
                    imageVector = Icons.Filled.AllInclusive,
                    contentDescription = "TruequeGo Logo",
                    modifier = Modifier.size(64.dp),
                    tint = PrimaryNavy
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "trueque",
                    style = MaterialTheme.typography.displayMedium,
                    color = PrimaryNavy
                )
                Text(
                    text = "GO",
                    style = MaterialTheme.typography.displayMedium,
                    color = TertiaryAccent
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Intercambia lo\nque ya no usas",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                color = PrimaryNavy,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Únete a la nueva era del trueque colaborativo sostenible. Dale valor a tus cosas y descubre algo mejor.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Comienza la experiencia",
                onClick = onNavigateToLogin,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    TruequeGoAPPsmovilesTheme {
        HomeScreen(onNavigateToLogin = {})
    }
}
