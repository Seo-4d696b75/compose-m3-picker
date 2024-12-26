package com.seo4d696b75.compose.material3.picker.sample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.seo4d696b75.compose.material3.picker.Picker
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
            listOf("零", "壱", "弐", "参", "肆", "伍", "陸", "七", "八", "玖", "拾")
        }
        var value by remember { mutableStateOf(values.first()) }

        val values2 = remember {
            listOf(
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 12, 2),
                LocalDate.of(2024, 12, 3),
                LocalDate.of(2024, 12, 4),
                LocalDate.of(2024, 12, 5),
                LocalDate.of(2024, 12, 6),
            )
        }

        var value2 by remember { mutableStateOf(values2.first()) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            Picker(
                value = value,
                values = values,
                onValueChange = { value = it },
            )
            Picker(
                value = value2,
                values = values2,
                labelSize = DpSize(120.dp, 48.dp),
                onValueChange = { value2 = it },
            )
        }
    }
}
