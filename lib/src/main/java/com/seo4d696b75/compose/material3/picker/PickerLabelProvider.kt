package com.seo4d696b75.compose.material3.picker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
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
    values: List<T>,
    label: @Composable (T, Boolean) -> Unit,
    enabled: Boolean,
    contentColor: Color,
): () -> PickerLabelProvider<T> {
    val provider = remember {
        PickerLabelProvider<T>()
    }.also {
        it.values = values
        it.label = label
        it.enabled = enabled
        it.contentColor = contentColor
    }
    return remember {
        { provider }
    }
}

/**
 * Defines how to render labels of picker
 */
@ExperimentalFoundationApi
internal class PickerLabelProvider<T> : LazyLayoutItemProvider {
    var values: List<T> by mutableStateOf(emptyList())
    var label: (@Composable (T, Boolean) -> Unit) by mutableStateOf({ _, _ -> })
    var enabled: Boolean by mutableStateOf(false)
    var contentColor: Color by mutableStateOf(Color.Unspecified)

    override val itemCount by derivedStateOf { values.size }

    @Composable
    override fun Item(index: Int, key: Any) {
        val value = values[index]
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
