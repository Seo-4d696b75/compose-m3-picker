package com.seo4d696b75.compose.material3.picker

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Creates and remember a [PickerState] with a conventional callback
 *
 * @param index An index of currently selected value. Must be included in range of [values].
 *   At the creation of [PickerState], this index is set as `initialIndex`.
 *   If [index] is changed outside of the picker (not by user interaction),
 *   the picker will be scrolled to the target position without animation.
 * @param values All the selectable values.
 * @param onIndexChange A callback invoked when the currently selected value in picker is changed.
 *   [onIndexChange] is NOT invoked while usr scrolling, but will be called
 *   after user interaction completed and the picker is settled to the final snapping position.
 *
 * @see [PickerState.settledIndex]
 */
@Composable
fun <T> rememberPickerState(
    index: Int,
    values: ImmutableList<T>,
    onIndexChange: (Int) -> Unit,
): PickerState<T> {
    val state = rememberPickerState(values, index)

    // value changed outside the picker
    LaunchedEffect(state, index) {
        if (state.settledIndex != index) {
            state.scrollToIndex(index)
        }
    }

    // invoke callback
    val latestIndex by rememberUpdatedState(index)
    val latestCallback by rememberUpdatedState(onIndexChange)
    LaunchedEffect(state) {
        snapshotFlow { state.settledIndex }
            .drop(1)
            .filter { it != latestIndex }
            .collect {
                latestCallback(it)
            }
    }

    return state
}

/**
 * Creates and remember a [PickerState] to be used with a [Picker].
 *
 * @param values All the selectable values.
 *   If [values] changed (`==` returns false), another state will be created and returned.
 * @param initialIndex The index of value which is selected at the first time.
 *   Must be in range of [values] size. While [values] remains the same (`==` returns true),
 *   change of [initialIndex] will have no effect.
 */
@Composable
fun <T> rememberPickerState(
    values: ImmutableList<T>,
    initialIndex: Int = 0,
): PickerState<T> = rememberSaveable(
    values,
    saver = PickerState.Saver(values),
) {
    PickerState(
        values = values,
        initialIndex = initialIndex,
    )
}

@Stable
class PickerState<out T> internal constructor(
    val values: List<T>,
    initialIndex: Int,
) {
    /**
     * Normalized offset of each displayed label.
     *
     * Note: offset of the currently selected value is 1,
     * because additional 2 labels on both sides (top and bottom) will be displayed.
     *
     * - `offset == 1` when the first item is currently selected
     * - `offset == 1-n` when the n-th (0 <= n < values.size) item is currently selected
     */
    val offset: Float
        get() = 1f - index

    /**
     * An index of currently selected value.
     *
     * This index may be non-integer while scrolling or snap (fling) animation running.
     */
    var index by mutableFloatStateOf(initialIndex.toFloat())
        private set

    /**
     * An index of value to which the current picker will be snapped.
     *
     * This index must be the same value of [index]
     * when no scroll or snap (fling) animation is running.
     * The snap position only takes account of the scroll offset,
     * not the current scroll (fling) velocity.
     */
    val currentIndex: Int by derivedStateOf { index.roundToInt() }

    /**
     * An index of value to which the picker should be snapped.
     *
     * Unlike [currentIndex], this index can only be updated when a user scrolling is completed
     * and the final snap position is determined.
     */
    var targetIndex: Int by mutableIntStateOf(initialIndex)
        internal set

    private var previousSettledIndex = initialIndex

    /**
     * An index of currently selected value.
     *
     * Unlike [currentIndex] or [targetIndex],
     * this index is NOT changed while user scrolling or snap (fling) animation running.
     */
    val settledIndex: Int by derivedStateOf {
        val current = this.index
        val target = this.targetIndex
        if ((target - current).absoluteValue < 1e-6) {
            previousSettledIndex = target
            target
        } else {
            previousSettledIndex
        }
    }

    /**
     * Scroll logic
     *
     * Same interface as `ScrollableState.dispatchRawDelta`
     */
    fun dispatchRawDelta(delta: Float): Float {
        val info = layoutInfo
        return if (info !is PickerLayoutInfo.Measured) {
            0f
        } else {
            val currentIndex = index
            val targetIndex =
                (currentIndex - delta / info.intervalHeight).coerceIn(0f, values.size - 1f)
            index = targetIndex
            -(targetIndex - currentIndex) * info.intervalHeight
        }
    }

    /**
     * Scroll to the specified index without animation.
     */
    fun scrollToIndex(index: Int) {
        val target = index.coerceIn(0, values.size - 1)
        this.index = target.toFloat()
        this.targetIndex = target
    }

    /**
     * Scroll to a given [index] with animation.
     */
    suspend fun animateScrollToIndex(
        index: Int,
        animationSpec: AnimationSpec<Float> = spring(),
    ) {
        val info = layoutInfo
        if (info !is PickerLayoutInfo.Measured) {
            scrollToIndex(index)
        } else {
            val target = index.coerceIn(0, values.size - 1)
            this.targetIndex = target
            val scrollAmount = -(target - this.index) * info.intervalHeight
            var previous = 0f
            animate(
                initialValue = 0f,
                targetValue = scrollAmount,
                animationSpec = animationSpec,
            ) { current, _ ->
                val delta = current - previous
                val consumed = dispatchRawDelta(delta)
                previous += consumed
            }
            this.index = target.toFloat()
        }
    }

    var layoutInfo: PickerLayoutInfo by mutableStateOf(PickerLayoutInfo.Zero)
        private set

    /**
     * Update layout size of the picker and get label indices to be displayed.
     */
    internal fun onLayout(
        labelHeight: Int,
        dividerHeight: Int,
    ): Iterable<Int> {
        layoutInfo = PickerLayoutInfo.Measured(labelHeight, dividerHeight)
        val lower = floor(index - 1).roundToInt()
        val upper = ceil(index + 1).roundToInt()
        return max(lower, 0)..min(upper, values.size - 1)
    }

    /**
     * Calculate offset in pixels at which the specified label should be placed.
     */
    fun offset(index: Int): Int = when (val info = layoutInfo) {
        PickerLayoutInfo.Zero -> throw IllegalStateException("picker not layout yet")
        is PickerLayoutInfo.Measured -> ((index + 1 - this.index) * info.intervalHeight).roundToInt()
    }

    companion object {
        fun <T> Saver(values: List<T>) = Saver<PickerState<T>, Int>(
            save = { it.settledIndex },
            restore = {
                PickerState(
                    values = values,
                    initialIndex = it,
                )
            }
        )
    }
}
