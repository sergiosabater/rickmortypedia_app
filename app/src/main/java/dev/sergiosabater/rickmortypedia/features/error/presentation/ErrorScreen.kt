package dev.sergiosabater.rickmortypedia.features.error.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sergiosabater.rickmortypedia.R
import dev.sergiosabater.rickmortypedia.core.ui.theme.RickMortyPediaTheme

@Composable
fun ErrorScreen(
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close App",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                        .size(48.dp)
                        .clickable { onExit() }
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.error),
                        contentDescription = "Error",
                        modifier = Modifier.size(200.dp)
                    )

                    Text(
                        text = "Oops! Something went wrong",
                        color = MaterialTheme.colorScheme.onBackground, // Usar color del tema
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = onRetry,
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary, // Usar color primario del tema
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Try Again", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorScreenPreview() {
    RickMortyPediaTheme {
        ErrorScreen(
            onRetry = { },
            onExit = { }
        )
    }
}