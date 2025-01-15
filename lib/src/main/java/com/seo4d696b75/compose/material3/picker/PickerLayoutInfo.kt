package com.seo4d696b75.compose.material3.picker

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PickerLayoutInfo {
    val labelHeight: Int
    val dividerHeight: Int

    val intervalHeight: Int
        get() = labelHeight + dividerHeight

    data object Zero : PickerLayoutInfo {
        override val labelHeight = 0
        override val dividerHeight = 0
    }

    data class Measured(
        override val labelHeight: Int,
        override val dividerHeight: Int,
    ) : PickerLayoutInfo
}
