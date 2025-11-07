package dev.sergiosabater.rickmortypedia.features.character.presentation.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sergiosabater.rickmortypedia.R

@Composable
fun SpeciesFilterBar(
    modifier: Modifier = Modifier,
    selectedSpecies: SpeciesFilter,
    onSpeciesSelected: (SpeciesFilter) -> Unit,
    availableSpecies: List<SpeciesFilter> = listOf(
        SpeciesFilter.ALL,
        SpeciesFilter.HUMAN,
        SpeciesFilter.ALIEN,
        SpeciesFilter.HUMANOID,
        SpeciesFilter.ROBOT,
        SpeciesFilter.ANIMAL
    )
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.filter_by_species),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = availableSpecies,
                key = { it.name }
            ) { species ->
                SpeciesFilterChip(
                    speciesFilter = species,
                    isSelected = selectedSpecies == species,
                    onClick = { onSpeciesSelected(species) }
                )
            }
        }
    }
}