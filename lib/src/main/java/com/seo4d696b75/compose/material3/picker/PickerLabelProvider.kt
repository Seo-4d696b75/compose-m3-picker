package com.seo4d696b75.compose.material3.picker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun <T> rememberPickerLabelProvider(
    state: PickerState<T>,
    label: @Composable (T, Boolean) -> Unit,
    enabled: Boolean,
    contentColor: Color,
): () -> PickerLabelProvider<T> {
    val provider = remember(state) {
        PickerLabelProvider(state)
    }.also {
        it.label = label
        it.enabled = enabled
        it.contentColor = contentColor
    }
    return remember(provider) {
        { provider }
    }
}

/**
 * Defines how to render labels of picker
 */
@ExperimentalFoundationApi
internal class PickerLabelProvider<T>(
    private val state: PickerState<T>,
) : LazyLayoutItemProvider,
    PickerValueSizeAwareScope by state {

    var label: (@Composable (T, Boolean) -> Unit) by mutableStateOf({ _, _ -> })
    var enabled: Boolean by mutableStateOf(false)
    var contentColor: Color by mutableStateOf(Color.Unspecified)

    override val itemCount: Int
        get() = when {
            valueSize == 1 -> 1

            state.isInfiniteScrollable && valueSize < 4 -> {
                // if values.size < 4 and infinite scrolling enabled,
                // multiple labels with a same index my be visible at the same time.
                // Note: While scrolling, up to 4 values are displayed simultaneously.
                // But LazyLayout does NOT allow to compose or measure a same index for multiple times,
                // so extra buffers are needed.
                valueSize * 2
            }

            else -> valueSize
        }

    @Composable
    override fun Item(index: Int, key: Any) {
        // if values.size < 4 and infinite scrolling enabled,
        // index my be out of range. Must be normalized.
        val value = state.values[index.normalizeValueIndex()]
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalContentColor provides contentColor
            ) {
                label(value, enabled)
            }
        }
    }
}
