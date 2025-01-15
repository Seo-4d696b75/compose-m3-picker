package com.seo4d696b75.compose.material3.picker

import androidx.annotation.FloatRange
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * A Picker which can be scrolled vertically.
 *
 * All the selectable values (labels) are ordered from top to bottom.
 * The currently selected value and neighbors (previous and next if any) are displayed.
 *
 * @param state The state to control this picker
 * @param modifier
 * @param enabled Whether a user can scroll this picker.
 *   Even if `false` set, the current selected value can be changed via [state] programmatically.
 * @param colors Colors of this picker
 * @param labelSize Size of each label. The actually displayed size may be smaller than [labelSize]
 *   if constraints of [modifier] is tight.
 * @param dividerHeight Height of a divider displayed between labels.
 *   The width of divider is same as the label.
 * @param labelMinAlpha Minimum alpha value of labels.
 *   The further the label position is from the picker center,
 *   the lower alpha value is applied.
 * @param flingBehavior How to control post scroll gestures (snapping and fling animation).
 * @param label How to render each value. This composable is placed in the middle of [labelSize].
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> Picker(
    state: PickerState<T>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PickerColors = PickerDefaults.colors(),
    labelSize: DpSize = PickerDefaults.labelSize,
    dividerHeight: Dp = PickerDefaults.dividerHeight,
    @FloatRange(from = 0.0, to = 1.0)
    labelMinAlpha: Float = 0.3f,
    flingBehavior: TargetedFlingBehavior = PickerDefaults.flingBehavior(state),
    label: @Composable (value: T, enabled: Boolean) -> Unit = { v, _ ->
        Text(v.toString())
    },
) {
    val labelProvider = rememberPickerLabelProvider(
        state = state,
        label = label,
        enabled = enabled,
        contentColor = colors.contentColor(enabled),
    )

    val mediator = remember {
        PickerDragMediator()
    }.also {
        it.state = state
        it.flingBehavior = flingBehavior
        it.scope = rememberCoroutineScope()
    }

    val gestureModifier = if (enabled) {
        Modifier.pointerInput(mediator) {
            detectVerticalDragGestures(
                onDragStart = mediator::onDragStart,
                onVerticalDrag = mediator::onDrag,
                onDragEnd = mediator::onDragEnd,
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(
                width = labelSize.width,
                height = labelSize.height * 3 + dividerHeight * 2,
            )
            .then(gestureModifier)
    ) {
        PickerLabels(
            state = state,
            labelProvider = labelProvider,
            labelSize = labelSize,
            labelMinAlpha = labelMinAlpha,
            dividerHeight = dividerHeight,
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dividerHeight)
                    .background(colors.dividerColor(enabled))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dividerHeight)
                    .background(colors.dividerColor(enabled))
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> PickerLabels(
    state: PickerState<T>,
    labelProvider: () -> PickerLabelProvider<T>,
    labelSize: DpSize,
    labelMinAlpha: Float,
    dividerHeight: Dp,
    modifier: Modifier = Modifier,
) {
    LazyLayout(
        itemProvider = labelProvider,
        modifier = modifier.clipToBounds(),
    ) { constraints ->
        val width = labelSize.width.roundToPx().coerceIn(
            minimumValue = constraints.minWidth,
            maximumValue = constraints.maxWidth,
        )
        val height = (labelSize.height * 3 + dividerHeight * 2).roundToPx().coerceIn(
            minimumValue = constraints.minHeight,
            maximumValue = constraints.maxHeight,
        )

        val dividerHeightPx = dividerHeight.toPx().roundToInt()
        val labelHeight = floor((height - dividerHeightPx * 2) / 3f).roundToInt()

        val indices = state.onLayout(labelHeight, dividerHeightPx)

        val labelConstraints = Constraints.fixed(
            width = width,
            height = labelHeight
        )
        val placeableMap = indices.associateWith { index ->
            measure(index, labelConstraints).first()
        }

        layout(width, height) {
            placeableMap.forEach { (index, placeable) ->
                placeable.placeWithLayer(
                    x = 0,
                    y = state.offset(index),
                ) {
                    val distance = (index - state.index).absoluteValue.coerceAtMost(1f)
                    alpha = 1f - (1f - labelMinAlpha) * distance
                }
            }
        }
    }
}

/**
 * Defines how to handle scroll and fling events
 */
private class PickerDragMediator : ScrollScope {
    private val velocityTracker = VelocityTracker()

    lateinit var state: PickerState<Any>
    lateinit var flingBehavior: TargetedFlingBehavior
    lateinit var scope: CoroutineScope

    fun onDragStart(point: Offset) {
        velocityTracker.resetTracking()
    }

    fun onDrag(change: PointerInputChange, amount: Float) {
        velocityTracker.addPosition(
            timeMillis = change.uptimeMillis,
            position = change.position,
        )
        // no dispatch to NestedScroll chains
        scrollBy(amount)
    }

    fun onDragEnd() {
        val velocity = velocityTracker.calculateVelocity()
        velocityTracker.resetTracking()

        scope.launch {
            with(flingBehavior) {
                // no dispatch to NestedScroll chains
                performFling(velocity.y)
            }
        }
    }

    override fun scrollBy(pixels: Float) = state.dispatchRawDelta(pixels)
}
