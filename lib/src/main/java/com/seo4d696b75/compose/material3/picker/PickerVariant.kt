package com.seo4d696b75.compose.material3.picker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import kotlinx.collections.immutable.ImmutableList

@Composable
fun NumberPicker(
    value: Int,
    range: ImmutableList<Int>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelStyle: TextStyle = PickerDefaults.labelStyle(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
) {
    Picker(
        index = range.indexOf(value),
        values = range,
        onIndexChange = { onValueChange(range[it]) },
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
    index: Int,
    values: ImmutableList<T>,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelStyle: TextStyle = PickerDefaults.labelStyle(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
) {
    Picker(
        state = rememberPickerState(index, values, onIndexChange),
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
