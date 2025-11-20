package com.seo4d696b75.compose.material3.picker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import kotlinx.collections.immutable.ImmutableList

/**
 * A Picker of integer values.
 *
 * Each value of [range] is displayed as a label by `Int.toString()`
 *
 * @param value currently selected value. Must be include in [range].
 * @param range all the values which can be selected
 * @param modifier
 * @param enabled Whether a user can scroll this picker.
 *   Even if `false` set, the current selected value can be changed via [value] programmatically.
 * @param colors color set of this picker
 * @param labelStyle `TextStyle` applied to labels
 * @param labelSize Size of each label. The actually displayed size may be smaller than [labelSize]
 *   if constraints of [modifier] is tight.
 * @param dividerHeight Height of a divider displayed between labels.
 *   The width of divider is same as the label.
 * @param isInfiniteScrollable Whether infinite scroll (including fling animation) is enabled.
 * @param onValueChange A callback invoked when the currently selected value is changed.
 *   This callback is NOT invoked while usr scrolling, but will be called
 *   after user interaction completed and the picker is settled to the final snapping position.
 */
@Composable
fun NumberPicker(
    value: Int,
    range: ImmutableList<Int>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelStyle: TextStyle = PickerDefaults.labelStyle(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
    isInfiniteScrollable: Boolean = false,
    onValueChange: (Int) -> Unit = {},
) {
    Picker(
        index = range.indexOf(value),
        values = range,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        labelStyle = labelStyle,
        labelSize = labelSize,
        dividerHeight = dividerHeight,
        isInfiniteScrollable = isInfiniteScrollable,
        onIndexChange = { onValueChange(range[it]) },
    )
}

/**
 * A Picker with simple labels implemented by `T.toString()`.
 *
 * @param index An index of currently selected value. Must be included in range of [values].
 *   If [index] is changed outside of the picker (not by user interaction),
 *   the picker will be scrolled to the target position without animation.
 * @param values All the selectable values.
 * @param modifier
 * @param enabled Whether a user can scroll this picker.
 *   Even if `false` set, the current selected value can be changed via [index] programmatically.
 * @param colors color set of this picker
 * @param labelStyle `TextStyle` applied to labels
 * @param labelSize Size of each label. The actually displayed size may be smaller than [labelSize]
 *   if constraints of [modifier] is tight.
 * @param dividerHeight Height of a divider displayed between labels.
 *   The width of divider is same as the label.
 * @param isInfiniteScrollable Whether infinite scroll (including fling animation) is enabled.
 * @param onIndexChange A callback invoked when the currently selected value in picker is changed.
 *   [onIndexChange] is NOT invoked while usr scrolling, but will be called
 *   after user interaction completed and the picker is settled to the final snapping position.
 *   The index param of [onIndexChange] is normalized in range of `0 ..< values.size`
 *   even when [isInfiniteScrollable] is `true`.
 */
@Composable
fun <T : Any> Picker(
    index: Int,
    values: ImmutableList<T>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelStyle: TextStyle = PickerDefaults.labelStyle(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
    isInfiniteScrollable: Boolean = false,
    onIndexChange: (Int) -> Unit = {},
) {
    Picker(
        state = rememberPickerState(index, values, isInfiniteScrollable, onIndexChange),
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
