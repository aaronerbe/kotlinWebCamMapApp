package com.example.kotlinwebcammapapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kotlinwebcammapapp.R

/**
 * A segmented button that allows the user to select multiple options.
 * selected filters are passed in as a list of strings to determine which icons to display
 * onfilterchange is used to update the list of selected filters.  the string in it is the filter that will toggle
 * @param selectedFilters The list of selected filters.
 * @param onFilterChange The callback that is called when a filter is changed.
 * @param modifier The modifier to be applied to the layout.
 * @return A [Row] layout that displays the segmented buttons.
 */
@Composable
fun MultiChoiceSegmentedButton(
    selectedFilters: List<String>,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // options is used in the when statement below to determine which icon to display

    val options = listOf("hiking", "camping", "caving", "mountain biking","snow sports")

    MultiChoiceSegmentedButtonRow {
        options.forEach { label ->
            val isSelected = selectedFilters.contains(label)
            val iconSize = 24.dp

            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = options.indexOf(label),
                    count = options.size
                ),
                checked = isSelected,
                onCheckedChange = { onFilterChange(label) },
                //can remove the .Icon(isSelected) portion to remove checkmarks if I want
                icon = { SegmentedButtonDefaults.Icon(isSelected) },
                //change background color to grey when it's selected
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                    activeContentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurface
                ),
                label = {
                    when (label) {
                        "hiking" -> Image(
                            painter = painterResource(id = R.drawable.hiking),
                            contentDescription = "Hiking",
                            modifier = Modifier.size(iconSize)
                        )
                        "camping" -> Image(
                            painter = painterResource(id = R.drawable.camping),
                            contentDescription = "Camping",
                            modifier = Modifier.size(iconSize)
                        )
                        "caving" -> Image(
                            painter = painterResource(id = R.drawable.caving),
                            contentDescription = "Caving",
                            modifier = Modifier.size(iconSize)
                        )
                        "mountain biking" -> Image(
                            painter = painterResource(id = R.drawable.mountain_biking),
                            contentDescription = "Mountain Biking",
                            modifier = Modifier.size(iconSize)
                        )
                        "trail running" -> Image(
                            painter = painterResource(id = R.drawable.trail_running),
                            contentDescription = "Trail Running",
                            modifier = Modifier.size(iconSize)
                        )
                        "snow sports" -> Image(
                            painter = painterResource(id = R.drawable.snow_sports),
                            contentDescription = "Snow Sports",
                            modifier = Modifier.size(iconSize)
                        )
                        "atv" -> Image(
                            painter = painterResource(id = R.drawable.atv),
                            contentDescription = "ATV",
                            modifier = Modifier.size(iconSize)
                        )
                        "horseback riding" -> Image(
                            painter = painterResource(id = R.drawable.horseback),
                            contentDescription = "Horseback Riding",
                            modifier = Modifier.size(iconSize)
                        )
                        else -> {}
                    }
                }
            )
        }
    }
}
