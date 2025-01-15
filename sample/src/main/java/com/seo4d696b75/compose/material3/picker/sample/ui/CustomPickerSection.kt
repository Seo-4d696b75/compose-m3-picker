package com.seo4d696b75.compose.material3.picker.sample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerSnapDistance
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.seo4d696b75.compose.material3.picker.Picker
import com.seo4d696b75.compose.material3.picker.PickerDefaults
import com.seo4d696b75.compose.material3.picker.rememberPickerState
import kotlinx.collections.immutable.persistentListOf

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
            persistentListOf(
                Icons.Outlined.Build,
                Icons.Outlined.MailOutline,
                Icons.Outlined.Call,
                Icons.Outlined.AccountCircle,
                Icons.Outlined.Create,
                Icons.Outlined.Delete,
            )
        }
        val state = rememberPickerState(values)

        Picker(
            state = state,
            enabled = true,
            colors = PickerDefaults.colors(contentColor = MaterialTheme.colorScheme.secondary),
            labelSize = DpSize(240.dp, 64.dp),
            dividerHeight = 4.dp,
            flingBehavior = PickerDefaults.flingBehavior(
                state = state,
                snapDistance = PagerSnapDistance.atMost(1),
            ),
        ) { icon, enabled ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
                Text(text = icon.name)
            }
        }
    }
}
