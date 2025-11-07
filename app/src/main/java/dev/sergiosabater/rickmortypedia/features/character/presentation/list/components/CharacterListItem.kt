package dev.sergiosabater.rickmortypedia.features.character.presentation.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.sergiosabater.rickmortypedia.R
import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.model.CharacterStatus
import dev.sergiosabater.rickmortypedia.core.ui.theme.RickMortyPediaTheme

@Composable
fun CharacterListItem(
    modifier: Modifier = Modifier,
    character: Character,
    onClick: (Character) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(character) }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image of ${character.name}",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_error)
            )

            Text(
                text = character.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "See details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(name = "Character List Item", showBackground = true)
@Composable
fun CharacterListItemPreview() {
    RickMortyPediaTheme {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CharacterListItem(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = CharacterStatus.ALIVE,
                    species = "Human",
                    type = "",
                    gender = "",
                    origin = "",
                    location = "",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episodeCount = 10,
                    originUrl = "",
                    locationUrl = "",
                    episodeUrls = listOf(),
                    url = "",
                    created = "",
                ),
                onClick = { /* Handle click */ }
            )

            CharacterListItem(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = CharacterStatus.ALIVE,
                    species = "Human",
                    type = "",
                    gender = "",
                    origin = "",
                    location = "",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episodeCount = 10,
                    originUrl = "",
                    locationUrl = "",
                    episodeUrls = listOf(),
                    url = "",
                    created = "",
                ),
                onClick = { /* Handle click */ }
            )
        }
    }
}

@Preview(name = "Character List Item - Placeholder", showBackground = true)
@Composable
fun CharacterListItemPlaceholderPreview() {
    RickMortyPediaTheme {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CharacterListItem(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = CharacterStatus.ALIVE,
                    species = "Human",
                    type = "",
                    gender = "",
                    origin = "",
                    location = "",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episodeCount = 10,
                    originUrl = "",
                    locationUrl = "",
                    episodeUrls = listOf(),
                    url = "",
                    created = "",
                ),
                onClick = { }
            )

            CharacterListItem(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = CharacterStatus.ALIVE,
                    species = "Human",
                    type = "",
                    gender = "",
                    origin = "",
                    location = "",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episodeCount = 10,
                    originUrl = "",
                    locationUrl = "",
                    episodeUrls = listOf(),
                    url = "",
                    created = "",
                ),
                onClick = { }
            )
        }
    }
}
