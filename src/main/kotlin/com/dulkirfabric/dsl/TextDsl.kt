@file:Suppress("NOTHING_TO_INLINE")

package com.dulkirfabric.dsl

import net.minecraft.text.MutableText
import net.minecraft.text.Text

@Deprecated(
    message = "Use extension property instead of extension function.",
    replaceWith = ReplaceWith("this.literal")
)
inline fun String.literal(): MutableText = Text.literal(this)

@Deprecated(
    message = "Use extension property instead of extension function.",
    replaceWith = ReplaceWith("this.literal")
)
inline fun String?.literal(): MutableText = this?.literal ?: Text.empty()

inline val String.literal: MutableText
    inline get() = Text.literal(this)

inline val String?.literal: MutableText
    inline get() = this?.literal ?: Text.empty()

inline val String.translatable: MutableText
    inline get() = Text.translatable(this)

inline val String?.translatable: MutableText
    inline get() = this?.translatable ?: Text.empty()