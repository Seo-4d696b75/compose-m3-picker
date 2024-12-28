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
import androidx.compose.ui.unit.dp
import com.seo4d696b75.compose.material3.picker.NumberPicker
import kotlinx.collections.immutable.toPersistentList

@Composable
fun NumberPickerSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "NumberPicker",
            style = MaterialTheme.typography.titleMedium,
        )
        val range = remember { (0..20).toPersistentList() }
        var value by remember { mutableIntStateOf(0) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            NumberPicker(
                value = value,
                range = range,
                onValueChange = { value = it },
            )
            NumberPicker(
                value = value,
                range = range,
                onValueChange = { value = it },
                enabled = false,
            )
        }
    }
}
