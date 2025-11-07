package dev.sergiosabater.rickmortypedia.features.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sergiosabater.rickmortypedia.R
import dev.sergiosabater.rickmortypedia.core.ui.theme.RickMortyPediaTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit,
    onError: () -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SplashContent(
        uiState = uiState,
        onLoadingComplete = onLoadingComplete,
        onError = onError
    )
}

@Composable
private fun SplashContent(
    uiState: SplashUiState,
    onLoadingComplete: () -> Unit,
    onError: () -> Unit
) {
    LaunchedEffect(uiState) {
        when (uiState) {
            is SplashUiState.Success -> {
                delay(500)
                onLoadingComplete()
            }

            is SplashUiState.Error -> {
                onError()
            }

            else -> { /* Loading */
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.loading),
                contentDescription = "Rick and Morty",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            CircularProgressIndicator(
                modifier = Modifier.size(51.dp),
                color = Color(0xFF00FF00),
                strokeWidth = 4.dp
            )

            Text(
                text = "Loading...",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun PreviewSplashScreenLoading() {
    RickMortyPediaTheme {
        SplashContent(
            uiState = SplashUiState.Loading,
            onLoadingComplete = {},
            onError = {}
        )
    }
}