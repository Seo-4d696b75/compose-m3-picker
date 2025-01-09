package com.seo4d696b75.compose.material3.picker

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Defines colors of a picker
 *
 * - [contentColor] is applied as [LocalContentColor] for `label` composable
 * - [dividersColor] is color of dividers placed between labels
 *
 * If the picker is disabled, `disabled**Color` are applied instead.
 */
@Immutable
class PickerColors(
    val contentColor: Color,
    val dividersColor: Color,
    val disabledContentColor: Color,
    val disabledDividerColor: Color,
) {
    @Stable
    fun contentColor(enabled: Boolean) = if (enabled) {
        contentColor
    } else {
        disabledContentColor
    }

    @Stable
    fun dividerColor(enabled: Boolean) = if (enabled) {
        dividersColor
    } else {
        disabledDividerColor
    }
}

/**
 * Default values used for [Picker]
 */
@Stable
object PickerDefaults {
    @Composable
    fun colors(
        // see M3 Date pickers > Enabled > Label text
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        // custom color
        dividersColor: Color = MaterialTheme.colorScheme.primary,
        // see M3 Date pickers > Disabled > Label text
        disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        // see M3 Switch > Disabled > Track
        disabledDividerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    ): PickerColors = PickerColors(
        contentColor = contentColor,
        dividersColor = dividersColor,
        disabledContentColor = disabledContentColor,
        disabledDividerColor = disabledDividerColor,
    )

    // color is provided via `LocalContentColor` with consideration of
    // whether the current picker is enabled or disabled
    @Composable
    fun labelStyle() = LocalTextStyle.current.copy(color = Color.Unspecified)

    val labelSize = DpSize(80.dp, 48.dp)

    val dividerHeight = 2.dp

    // inspired by PagerDefaults.flingBehavior()
    /**
     * Gets a [snapFlingBehavior] which will snap labels to the center of the layout.
     *
     * The snapping and fling (scroll with large velocity) animation
     * can be controlled with the given params.
     *
     * @param state The picker state to which this FlingBehavior applied to.
     * @param flingEnabled If `false` set, only snapping animation will be running
     *   even when scroll completed with large velocity.
     * @param decayAnimationSpec The animation spec used to approach the target offset
     *   when the fling velocity is large enough.
     * @param snapAnimationSpec The animation spec used to finally snap to the position.
     */
    @Composable
    fun flingBehavior(
        state: PickerState<Any>,
        flingEnabled: Boolean = true,
        decayAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay(),
        snapAnimationSpec: AnimationSpec<Float> = spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = Int.VisibilityThreshold.toFloat(),
        ),
    ): TargetedFlingBehavior =
        remember(state, flingEnabled, decayAnimationSpec, snapAnimationSpec) {
            snapFlingBehavior(
                snapLayoutInfoProvider = PickerSnapLayoutProvider(state, flingEnabled),
                decayAnimationSpec = decayAnimationSpec,
                snapAnimationSpec = snapAnimationSpec,
            )
        }
}
