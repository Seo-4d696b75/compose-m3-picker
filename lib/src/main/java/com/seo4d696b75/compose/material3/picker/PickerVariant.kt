package com.seo4d696b75.compose.material3.picker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@Composable
fun NumberPicker(
    value: Int,
    range: Iterable<Int>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelStyle: TextStyle = PickerDefaults.labelStyle(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
) {
    Picker(
        value = value,
        values = range.toList(),
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        labelStyle = labelStyle,
        labelSize = labelSize,
        dividerHeight = dividerHeight,
    )
}

@Composable
fun <T : Any> Picker(
    value: T,
    values: List<T>,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelStyle: TextStyle = PickerDefaults.labelStyle(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
) {
    Picker(
        state = rememberPickerState(value, values, onValueChange),
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        labelSize = labelSize,
        dividerHeight = dividerHeight,
    ) { v, _ ->
        Text(
            text = v.toString(),
            style = labelStyle,
        )
    }
}
