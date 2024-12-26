package com.seo4d696b75.compose.material3.picker.sample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Icon
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
import com.seo4d696b75.compose.material3.picker.PickerDefaults
import com.seo4d696b75.compose.material3.picker.rememberPickerState

@Composable
fun CustomPickerSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "CustomPicker",
            style = MaterialTheme.typography.titleMedium,
        )
        val values = remember {
            listOf(
                Icons.Outlined.Build,
                Icons.Outlined.MailOutline,
                Icons.Outlined.Call,
                Icons.Outlined.AccountCircle,
                Icons.Outlined.Create,
                Icons.Outlined.Delete,
            )
        }
        var value by remember { mutableStateOf(values.first()) }
        val state = rememberPickerState(value, values) { value = it }

        Picker(
            state = state,
            enabled = true,
            colors = PickerDefaults.colors(contentColor = MaterialTheme.colorScheme.secondary),
            labelSize = DpSize(240.dp, 64.dp),
            dividerHeight = 4.dp,
            flingBehavior = PickerDefaults.flingBehavior(state = state, flingEnabled = false),
        ) { current, enabled ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = current,
                    contentDescription = null,
                )
                Text(text = current.name)
            }
        }
    }
}
