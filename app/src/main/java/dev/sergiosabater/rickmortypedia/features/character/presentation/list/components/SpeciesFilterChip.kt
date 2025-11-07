package dev.sergiosabater.rickmortypedia.features.character.presentation.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sergiosabater.rickmortypedia.R

@Composable
fun SpeciesFilterChip(
    modifier: Modifier = Modifier,
    speciesFilter: SpeciesFilter,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "background_color"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "text_color"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected)
            Color.Transparent
        else
            MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 300),
        label = "border_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(speciesFilter.displayNameRes),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

//Species available in the filter
enum class SpeciesFilter(val displayNameRes: Int, val filterValue: String?) {
    ALL(R.string.species_filter_all, null),
    HUMAN(R.string.species_filter_human, "Human"),
    ALIEN(R.string.species_filter_alien, "Alien"),
    HUMANOID(R.string.species_filter_humanoid, "Humanoid"),
    ROBOT(R.string.species_filter_robot, "Robot"),
    ANIMAL(R.string.species_filter_animal, "Animal");
}