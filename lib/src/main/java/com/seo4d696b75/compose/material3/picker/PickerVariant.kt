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
 * @param onValueChange A callback invoked when the currently selected value is changed.
 *   This callback is NOT invoked while usr scrolling, but will be called
 *   after user interaction completed and the picker is settled to the final snapping position.
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
 */
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
    isInfiniteScrollable: Boolean = false,
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
        isInfiniteScrollable = isInfiniteScrollable,
    )
}

/**
 * A Picker with simple labels implemented by `T.toString()`.
 *
 * @param index An index of currently selected value. Must be included in range of [values].
 *   If [index] is changed outside of the picker (not by user interaction),
 *   the picker will be scrolled to the target position without animation.
 * @param values All the selectable values.
 * @param onIndexChange A callback invoked when the currently selected value in picker is changed.
 *   [onIndexChange] is NOT invoked while usr scrolling, but will be called
 *   after user interaction completed and the picker is settled to the final snapping position.
 *   The index param of [onIndexChange] is normalized in range of `0 ..< values.size`
 *   even when [isInfiniteScrollable] is `true`.
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
 */
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
    isInfiniteScrollable: Boolean = false,
) {
    Picker(
        state = rememberPickerState(index, values, onIndexChange, isInfiniteScrollable),
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
