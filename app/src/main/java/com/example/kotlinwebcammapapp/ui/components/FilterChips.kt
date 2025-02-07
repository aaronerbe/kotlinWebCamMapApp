package com.example.kotlinwebcammapapp.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChips(
    selectedFilters: Set<String>,
    onFilterToggle: (String) -> Unit
) {
    val filterOptions = listOf("Hiking", "Camping", "Mountain Biking", "Caving", "Trail Running", "Snow Sports", "ATV", "Horseback Riding")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Filter Trails", style = MaterialTheme.typography.titleMedium)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // ✅ 2 columns for better spacing
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterOptions) { filter ->
                FilterChip(
                    selected = selectedFilters.contains(filter),
                    onClick = { onFilterToggle(filter) },
                    label = { Text(filter) }
                )
            }
        }
    }
}
