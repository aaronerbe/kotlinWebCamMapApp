import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterDropdownMenu(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier // ✅ Add this modifier parameter
) {
    var expanded by remember { mutableStateOf(false) }
    val filterOptions = listOf("All", "Hiking", "Camping", "Mountain Biking", "Caving", "Trail Running", "Snow Sports", "ATV", "Horseback Riding")

    Box(
        modifier = modifier.fillMaxWidth(), // ✅ Apply modifier here
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.width(200.dp) // ✅ Set a fixed width
        ) {
            Text(text = selectedFilter)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand Filters")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp) // ✅ Matches button width
        ) {
            filterOptions.forEach { filter ->
                DropdownMenuItem(
                    text = { Text(filter) },
                    onClick = {
                        onFilterSelected(filter) // ✅ Update filter selection
                        expanded = false
                    }
                )
            }
        }
    }
}
