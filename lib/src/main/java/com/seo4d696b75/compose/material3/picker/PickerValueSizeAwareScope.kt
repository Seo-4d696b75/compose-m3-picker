package com.seo4d696b75.compose.material3.picker

/**
 * Abstracts index constraints based on data size reference and whether infinite scrolling is enabled
 */
internal interface PickerValueSizeAwareScope {
    val valueSize: Int
    fun Int.normalizeValueIndex(): Int
    fun Int.coerceInValueIndices(): Int
    fun Float.coerceInValueIndices(): Float
}

internal fun pickerValueSizeAwareScope(
    values: List<*>,
    isInfiniteScrollable: Boolean,
) = object : PickerValueSizeAwareScope {
    override val valueSize = values.size

    override fun Int.normalizeValueIndex() = this.mod(valueSize)

    override fun Int.coerceInValueIndices() = if (isInfiniteScrollable) {
        this
    } else {
        this.coerceIn(0, valueSize - 1)
    }

    override fun Float.coerceInValueIndices() = if (isInfiniteScrollable) {
        this
    } else {
        this.coerceIn(0f, valueSize - 1f)
    }
}
