package com.seo4d696b75.compose.material3.picker

import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Defines how to calculate target offsets of snapping and fling animation
 */
internal class PickerSnapLayoutProvider(
    private val state: PickerState<Any>,
    private val flingEnabled: Boolean,
) : SnapLayoutInfoProvider {
    override fun calculateSnapOffset(velocity: Float): Float {
        // ignore sign of velocity
        val snapIndex = state.currentIndex
        val interval = state.intervalHeight
        return if (interval.isNaN()) {
            0f
        } else {
            state.targetIndex = snapIndex
            -(snapIndex - state.index) * interval
        }
    }

    override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float {
        if (!flingEnabled) {
            // use snapping instead if fling animation is disabled
            return 0f
        }
        val interval = state.intervalHeight
        if (interval.isNaN()) {
            return 0f
        }
        val currentIndex = state.index
        val decayIndex = currentIndex - decayOffset / interval
        val snapIndex = decayIndex.roundToInt().coerceIn(0, state.values.size - 1)
        val distance = (snapIndex - currentIndex).absoluteValue
        return if (distance <= 1f) {
            // if current index is close enough to the target index, no decay animation
            0f
        } else {
            state.targetIndex = snapIndex
            distance * interval * velocity.sign
        }
    }
}
