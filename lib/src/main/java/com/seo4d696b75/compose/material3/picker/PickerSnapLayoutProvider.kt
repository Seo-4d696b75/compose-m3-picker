package com.seo4d696b75.compose.material3.picker

import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import kotlin.math.roundToInt

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
            return calculateSnapOffset(velocity)
        }
        val interval = state.intervalHeight
        return if (interval.isNaN()) {
            0f
        } else {
            val currentIndex = state.index
            val decayIndex = currentIndex - decayOffset / interval
            val snapIndex = decayIndex.roundToInt().coerceIn(0, state.values.size - 1)
            state.targetIndex = snapIndex
            -(snapIndex - currentIndex) * interval
        }
    }
}
