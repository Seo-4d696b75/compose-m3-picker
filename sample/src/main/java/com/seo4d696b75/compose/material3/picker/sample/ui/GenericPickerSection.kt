package com.seo4d696b75.compose.material3.picker.sample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.seo4d696b75.compose.material3.picker.Picker
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Composable
fun GenericPickerSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "GenericPicker",
            style = MaterialTheme.typography.titleMedium,
        )
        val values = remember {
            persistentListOf("零", "壱", "弐", "参", "肆", "伍", "陸", "七", "八", "玖", "拾")
        }
        var index by remember { mutableIntStateOf(0) }

        val values2 = remember {
            persistentListOf(
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 12, 2),
                LocalDate.of(2024, 12, 3),
                LocalDate.of(2024, 12, 4),
                LocalDate.of(2024, 12, 5),
                LocalDate.of(2024, 12, 6),
            )
        }

        var index2 by remember { mutableIntStateOf(0) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            Picker(
                index = index,
                values = values,
                onIndexChange = { index = it },
            )
            Picker(
                index = index2,
                values = values2,
                labelSize = DpSize(120.dp, 48.dp),
                onIndexChange = { index2 = it },
            )
        }
    }
}
