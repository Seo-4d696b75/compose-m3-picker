package com.seo4d696b75.compose.material3.picker

import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.pager.PagerSnapDistance
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Defines how to calculate target offsets of snapping and fling animation
 */
internal class PickerSnapLayoutProvider(
    private val state: PickerState<Any>,
    private val velocityThreshold: Float,
    private val snapDistance: PagerSnapDistance,
) : SnapLayoutInfoProvider,
    PickerValueSizeAwareScope by state {
    override fun calculateSnapOffset(velocity: Float): Float {
        val currentIndex = state.index
        val targetIndex = if (velocity.absoluteValue < velocityThreshold) {
            // ignore sign of velocity and snap to the closest
            currentIndex.roundToInt()
        } else {
            // next index
            if (velocity > 0) {
                floor(currentIndex).roundToInt()
            } else {
                ceil(currentIndex).roundToInt()
            }
        }.coerceInValueIndices()

        return when (val info = state.layoutInfo) {
            PickerLayoutInfo.Zero -> 0f

            is PickerLayoutInfo.Measured -> {
                state.targetIndex = targetIndex
                -(targetIndex - currentIndex) * info.intervalHeight
            }
        }
    }

    override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float {
        val info = state.layoutInfo as? PickerLayoutInfo.Measured ?: return 0f
        val currentIndex = state.index
        val startIndex = if (velocity > 0) {
            ceil(currentIndex).roundToInt()
        } else {
            floor(currentIndex).roundToInt()
        }

        val indexOffset = (decayOffset / info.intervalHeight).toInt()
        val suggestedTargetIndex = (startIndex - indexOffset).coerceInValueIndices()

        // Apply the snap distance suggestion.
        val targetIndex = snapDistance.calculateTargetPage(
            startPage = startIndex,
            suggestedTargetPage = suggestedTargetIndex,
            pageSize = info.labelHeight,
            pageSpacing = info.dividerHeight,
            velocity = velocity,
        ).coerceInValueIndices()

        val distance = (targetIndex - startIndex).absoluteValue

        // if current index is close enough to the target index, no decay animation
        val decayDistance = (distance - 1f).coerceAtLeast(0f)

        return if (decayDistance == 0f) {
            0f
        } else {
            state.targetIndex = targetIndex
            decayDistance * info.intervalHeight * velocity.sign
        }
    }
}
